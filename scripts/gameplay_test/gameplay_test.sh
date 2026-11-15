#!/bin/bash
# usage: gameplay_test.sh <gradle module, e.g. v1_21_1:fabric> <world name> <log file>
# Starts the module's dedicated server on a fresh FLAT world, builds small test set-ups at y=200,
# lets them run and says what became of them. Tests the gameplay features (docs/MERGE_PLAN.md,
# phase 3): grass and mycelium spreading, sheep, layers falling with sand, ore layer drops.
# Read the result with:
#   grep -E "COUNT|CHECK|ORE |LEFT|filled|No blocks|entity data|Test (passed|failed)|ALG gameplay" <log file>
# and compare it with the expected values in the phase 3 notes of docs/MERGE_PLAN.md.
#
# Nobody is on the server, so two things a player normally does go through the mod's test command:
# "algdebug gameplay anchor" (spreading happens around players) and "algdebug gameplay mine"
# (breaks a block as a player holding a netherite pickaxe).
#
# Run from the repo root with JAVA_HOME = the JDK the module's server needs. server.properties is
# put back as it was at the end; the world folder <run>/<world name> is left for you to delete.
# Never creates eula.txt.
#
# Per version: from 1.21.2 an empty server pauses after 60 s (pause-when-empty-seconds, set to 0
# here); 1.21.11 renamed the game rules (doDaylightCycle is advance_time, both are sent, the
# unknown one fails harmlessly); 1.20.1 has no "tick sprint", so it runs in real time with a raised
# randomTickSpeed for the long run instead (spreading scales with it; sheep get fewer ticks, so
# fewer layers are eaten than on the other versions).
mod=$1; world=$2; log=$3
run="${mod//://}/run"
props="$run/server.properties"
legacy=0; case $mod in v1_20_1:*) legacy=1;; esac
cp "$props" "$log.server.properties.bak"
trap 'cp "$log.server.properties.bak" "$props"; rm -f "$log.server.properties.bak"' EXIT
sed -i -e "s/^level-name=.*/level-name=$world/" -e "s/^level-seed=.*/level-seed=7474/" \
       -e 's/^level-type=.*/level-type=minecraft\\:flat/' \
       -e 's/^pause-when-empty-seconds=.*/pause-when-empty-seconds=0/' "$props"
: > "$log"

# wait_for <pattern> <times> <seconds>: until the log holds the pattern that many times
wait_for() {
  local t=0
  until [ "$(grep -c "$1" "$log" 2>/dev/null)" -ge "$2" ]; do
    sleep 0.5; t=$((t+1)); [ $t -gt $(($3*2)) ] && { echo "say TIMEOUT waiting for $1"; return 1; }
  done
}

# advance <ticks> <sprints so far, this one included>: lets that much game time pass. On 1.20.1
# (no tick sprint): the short run in real time; the long one 2 minutes at randomTickSpeed 300,
# about 80 times the spreading of the same time at 3.
advance() {
  if [ $legacy = 1 ]; then
    if [ "$1" -gt 2000 ]; then
      echo "gamerule randomTickSpeed 300"; sleep 120; echo "gamerule randomTickSpeed 3"
    else
      sleep $(( $1 / 20 ))
    fi
  else
    echo "tick sprint $1"
    wait_for 'Sprint completed' "$2" 600
  fi
}

# count <label> <fill command>: "fill ... replace X" says how many X there were. Destructive, so
# only used once a test is over.
count() { echo "say COUNT $1"; echo "$2"; sleep 0.3; }
# block <label> <x y z> <block>: is this block there?
block() {
  echo "execute if block $2 $3 run say CHECK $1 yes"
  echo "execute unless block $2 $3 run say CHECK $1 no"
  sleep 0.2
}

{
  until grep -q 'Done (' "$log" 2>/dev/null; do
    grep -q 'BUILD FAILED\|Exception in thread "main"' "$log" 2>/dev/null && exit 0
    sleep 1
  done
  sleep 2
  echo "forceload add 0 0 63 47"
  echo "gamerule doDaylightCycle false"
  echo "gamerule advance_time false"
  echo "time set noon"
  sleep 3

  # ---------- spreading: four patches of dirt layers in chunk 0,0 ----------
  echo "fill 0 199 0 15 199 15 minecraft:stone"
  # A: a grass layer among dirt layers, with a row of dirt blocks next to them
  echo "fill 0 200 0 4 200 4 aronalayers:dirt_layer[layers=3]"
  echo "setblock 2 200 2 aronalayers:grass_layer[layers=3]"
  echo "fill 0 200 5 4 200 5 minecraft:dirt"
  # B: a grass block among dirt layers
  echo "fill 8 200 0 12 200 4 aronalayers:dirt_layer[layers=5]"
  echo "setblock 10 200 2 minecraft:grass_block"
  # C: a mycelium layer among dirt layers
  echo "fill 0 200 9 4 200 13 aronalayers:dirt_layer[layers=2]"
  echo "setblock 2 200 11 aronalayers:mycelium_layer[layers=2]"
  # D: dirt layers alone
  echo "fill 8 200 9 12 200 13 aronalayers:dirt_layer[layers=3]"
  echo "algdebug gameplay anchor 2 200 2"

  # ---------- sheep: two glass pens in chunk 3,1, outside the 3x3 chunks of the anchor ----------
  # (inside them, the grass around an eaten layer would grow back over it)
  # pen 1: 2-high grass layers lying on grass blocks. pen 2: full-height grass layers on stone.
  echo "fill 48 199 19 58 199 23 minecraft:stone"
  echo "fill 49 199 20 51 199 22 minecraft:grass_block"
  echo "fill 48 200 19 52 201 23 minecraft:glass"
  echo "fill 49 200 20 51 201 22 minecraft:air"
  echo "fill 49 200 20 51 200 22 aronalayers:grass_layer[layers=2]"
  echo "fill 54 200 19 58 202 23 minecraft:glass"
  echo "fill 55 200 20 57 202 22 minecraft:air"
  echo "fill 55 200 20 57 200 22 aronalayers:grass_layer[layers=8]"
  for p in "49.5 200.3 20.5" "51.5 200.3 20.5" "49.5 200.3 22.5" "51.5 200.3 22.5" \
           "55.5 201 20.5" "57.5 201 20.5" "55.5 201 22.5" "57.5 201 22.5"; do
    echo "summon minecraft:sheep $p"
  done
  sleep 1

  # A short run first: a few layers eaten, each once. In pen 1 an eaten layer must still lie on
  # its grass block ("layer only"). Later a sheep grazing on a dirt layer eats the grass block
  # under it, as vanilla sheep do through snow, so this can only be told apart early.
  advance 1500 1
  sleep 1
  for x in 49 50 51; do for z in 20 21 22; do
    echo "execute if block $x 200 $z aronalayers:dirt_layer if block $x 199 $z minecraft:grass_block run say CHECK pen1 early: layer only eaten"
    echo "execute if block $x 200 $z aronalayers:dirt_layer unless block $x 199 $z minecraft:grass_block run say CHECK pen1 early: layer and block eaten"
    echo "execute unless block $x 200 $z aronalayers:dirt_layer unless block $x 199 $z minecraft:grass_block run say CHECK pen1 early: BLOCK EATEN UNDER A GRASS LAYER"
  done; done
  sleep 0.5

  advance 34500 2
  sleep 1

  count "A dirt layers left of 24" "fill 0 200 0 4 200 4 minecraft:stone replace aronalayers:dirt_layer"
  count "A grass layers of 25" "fill 0 200 0 4 200 4 minecraft:stone replace aronalayers:grass_layer[layers=3]"
  count "A dirt blocks left of 5" "fill 0 200 5 4 200 5 minecraft:stone replace minecraft:dirt"
  count "B dirt layers left of 24" "fill 8 200 0 12 200 4 minecraft:stone replace aronalayers:dirt_layer"
  count "B grass layers of 24" "fill 8 200 0 12 200 4 minecraft:stone replace aronalayers:grass_layer[layers=5]"
  count "C dirt layers left of 24" "fill 0 200 9 4 200 13 minecraft:stone replace aronalayers:dirt_layer"
  count "C mycelium layers of 25" "fill 0 200 9 4 200 13 minecraft:stone replace aronalayers:mycelium_layer[layers=2]"
  count "D dirt layers left of 25" "fill 8 200 9 12 200 13 minecraft:stone replace aronalayers:dirt_layer[layers=3]"
  echo "algdebug gameplay anchor clear"

  echo "say COUNT sheep of 8"; echo "execute if entity @e[type=minecraft:sheep]"; sleep 0.3
  count "pen1 eaten layers (dirt layers, 2 high) of 9" "fill 49 200 20 51 200 22 minecraft:stone replace aronalayers:dirt_layer[layers=2]"
  count "pen1 uneaten layers of 9" "fill 49 200 20 51 200 22 minecraft:stone replace aronalayers:grass_layer[layers=2]"
  count "pen2 eaten layers (dirt layers, 8 high) of 9" "fill 55 200 20 57 200 22 minecraft:stone replace aronalayers:dirt_layer[layers=8]"
  count "pen2 uneaten layers of 9" "fill 55 200 20 57 200 22 minecraft:stone replace aronalayers:grass_layer[layers=8]"
  echo "kill @e[type=minecraft:sheep]"; sleep 0.5; echo "kill @e[type=minecraft:item]"

  # ---------- falling: columns in chunk 2,0, floor at y=199 ----------
  echo "fill 32 199 0 47 199 15 minecraft:stone"
  # F1: sand under a full layer and a 3-high layer, both also held by stone beside them
  echo "setblock 34 203 2 minecraft:stone"; echo "setblock 34 204 2 minecraft:sand"
  echo "setblock 35 205 2 minecraft:stone"; echo "setblock 35 206 2 minecraft:stone"
  echo "setblock 34 205 2 aronalayers:dirt_layer[layers=8]"; echo "setblock 34 206 2 aronalayers:grass_layer[layers=3]"
  # F2: a layer set against a wall above sand
  echo "setblock 38 203 2 minecraft:stone"; echo "setblock 38 204 2 minecraft:sand"
  echo "setblock 38 205 1 minecraft:stone"; echo "setblock 38 205 2 aronalayers:dirt_layer[facing=north,layers=3]"
  # F3: two low layers; the upper one lands on the lower one, next to a block that can hold it
  echo "setblock 42 203 2 minecraft:stone"; echo "setblock 42 204 2 minecraft:sand"
  echo "setblock 42 205 2 aronalayers:dirt_layer[layers=3]"
  echo "setblock 43 206 2 minecraft:stone"; echo "setblock 42 206 2 aronalayers:grass_layer[layers=2]"
  echo "setblock 43 202 2 minecraft:stone"
  # F4: sand a player breaks, under a layer also held by stone beside it
  echo "setblock 34 200 8 minecraft:sand"; echo "setblock 35 201 8 minecraft:stone"
  echo "setblock 34 201 8 aronalayers:dirt_layer[layers=4]"
  # F5: a block that is not a layer (falls only if it is in additional_falling_blocks)
  echo "setblock 38 203 8 minecraft:stone"; echo "setblock 38 204 8 minecraft:sand"
  echo "setblock 39 205 8 minecraft:stone"; echo "setblock 38 205 8 minecraft:oak_slab"
  # F6: gravel under a 3D layer
  echo "setblock 42 203 8 minecraft:stone"; echo "setblock 42 204 8 minecraft:gravel"
  echo "setblock 42 205 7 minecraft:stone"
  echo "setblock 42 205 8 aronalayers:stone_3d_layer[vertical_layers=2,horizontal_layers=3,horizontal_facing=north]"
  sleep 2
  for c in "34 203 2" "38 203 2" "42 203 2" "38 203 8" "42 203 8"; do echo "setblock $c minecraft:air"; done
  echo "algdebug gameplay mine 34 200 8"
  sleep 6
  block "F1 sand landed" "34 200 2" "minecraft:sand"
  block "F1 full layer on the sand" "34 201 2" "aronalayers:dirt_layer[layers=8,facing=down]"
  block "F1 low layer on top" "34 202 2" "aronalayers:grass_layer[layers=3,facing=down]"
  block "F1 old place empty" "34 205 2" "minecraft:air"
  block "F2 sand landed" "38 200 2" "minecraft:sand"
  block "F2 wall layer stayed" "38 205 2" "aronalayers:dirt_layer[facing=north,layers=3]"
  block "F3 lower layer on the sand" "42 201 2" "aronalayers:dirt_layer[layers=3]"
  block "F3 upper layer placed above it" "42 202 2" "aronalayers:grass_layer[layers=2]"
  block "F4 layer fell where the sand was" "34 200 8" "aronalayers:dirt_layer[layers=4]"
  block "F4 old place empty" "34 201 8" "minecraft:air"
  block "F5 slab stayed" "38 205 8" "minecraft:oak_slab"
  block "F5 slab fell" "38 201 8" "minecraft:oak_slab"
  block "F6 gravel landed" "42 200 8" "minecraft:gravel"
  block "F6 3D layer on the gravel" "42 201 8" "aronalayers:stone_3d_layer[vertical_layers=2,horizontal_layers=3]"
  echo "say COUNT falling items (F4's sand item is expected)"
  echo "execute positioned 40 201 5 as @e[type=minecraft:item,distance=..16] run data get entity @s Item"
  sleep 0.5
  echo "kill @e[type=minecraft:item]"

  # ---------- ore drops: one glass pit in chunk 0,2, one block at a time ----------
  echo "fill 0 199 32 15 199 47 minecraft:stone"
  echo "fill 2 200 34 6 201 38 minecraft:glass"
  echo "fill 3 200 35 5 201 37 minecraft:air"
  ore() { # <label> <block> <mine option>
    echo "kill @e[type=minecraft:item]"; echo "kill @e[type=minecraft:experience_orb]"
    echo "setblock 4 200 36 $2"
    sleep 0.4
    echo "say ORE $1"
    echo "algdebug gameplay mine 4 200 36${3:+ $3}"
    sleep 0.4
    echo "execute positioned 4 200 36 as @e[type=minecraft:item,distance=..5] run data get entity @s Item"
    echo "execute positioned 4 200 36 if entity @e[type=minecraft:experience_orb,distance=..8]"
    echo "execute if block 4 200 36 minecraft:air run say LEFT air"
    for n in 1 2 3 4 5 6 7; do echo "execute if block 4 200 36 $(echo "$2" | sed 's/\[.*//')[layers=$n] run say LEFT $n layers"; done
    echo "setblock 4 200 36 minecraft:air"
    sleep 0.4
  }
  ore "coal 5 plain"            "aronalayers:coal_ore_layer[layers=5]" ""
  ore "coal 5 silk"             "aronalayers:coal_ore_layer[layers=5]" "silk_touch"
  ore "deepslate copper 8 plain" "aronalayers:deepslate_copper_ore_layer[layers=8]" ""
  ore "redstone 8 plain"        "aronalayers:redstone_ore_layer[layers=8]" ""
  ore "diamond 4 fortune"       "aronalayers:diamond_ore_layer[layers=4]" "fortune"
  ore "diamond 4 plain"         "aronalayers:diamond_ore_layer[layers=4]" ""
  ore "iron 6 sneak"            "aronalayers:iron_ore_layer[layers=6]" "sneak"
  ore "iron 6 sneak silk"       "aronalayers:iron_ore_layer[layers=6]" "sneak_silk_touch"
  ore "stone layer 3 plain"     "aronalayers:stone_layer[layers=3]" ""
  ore "ancient debris 2 plain"  "aronalayers:ancient_debris_layer[layers=2]" ""

  echo "say TESTS DONE"
  sleep 1
  echo "stop"
} | ./gradlew.bat ":$mod:runServer" --console=plain >> "$log" 2>&1
echo "EXIT=$?" >> "$log"
