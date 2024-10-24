# -----{ 3.3.0 }-----

1.21.3

# -----{ 3.0.11 }-----

Improved performance for certain UI render utilities.
Fix crash with Tech Reborn on fabric

# -----{ 3.0.10 }-----



# -----{ 3.0.9 }-----



# -----{ 3.0.8 }-----

Fix extra parameter in CodecSavedData
Added CodecSavedData

# -----{ 3.0.7 }-----

Added CodecSavedData
Added NetworkHandle and bumped ByteCodecs

# -----{ 3.0.6 }-----

Added NetworkHandle and bumped ByteCodecs
Deprecated PacketType.type, added helpers for CodecPacketType

# -----{ 3.0.5 }-----

Deprecated PacketType.type, added helpers for CodecPacketType

# -----{ 3.0.4 }-----

Fixed crash on dedicated server

# -----{ 3.0.3 }-----

Fixed crash on dedicated server

# -----{ 3.0.2 }-----

fixed crash if someone used stack sensitive texture on neo

# -----{ 3.0.1 }-----

fixed crash if someone used stack sensitive texture on neo

# -----{ 3.0.0 }-----



# -----{ 2.5.5 }-----

Added helper for mojang either in bytecodecs.
Update bytecodecs to 1.1.0

# -----{ 2.5.4 }-----

Update bytecodecs to 1.1.0

# -----{ 2.5.3 }-----

Fixed fabric network packets being flipped
Added helper for creating data components & added pair bytecodec

# -----{ 2.5.2 }-----

Added helper for creating data components & added pair bytecodec

# -----{ 2.5.1 }-----



# -----{ 2.5.0 }-----

Updated to 1.20.5

# -----{ 2.4.10 }-----

Added option to stop resetting mouse on container open.
Add menu content player context from 1.20.1 to 1.20.4

# -----{ 2.4.9 }-----

Add menu content player context from 1.20.1 to 1.20.4
Updated highlight internals to use less memory

# -----{ 2.4.8 }-----

Updated highlight internals to use less memory

# -----{ 2.4.7 }-----

Added required option to packs and allow loading datapacks

# -----{ 2.4.6 }-----

Check if the mouse is in bounds to try cursor checks.

# -----{ 2.4.5 }-----

Check if the mouse is in bounds to try cursor checks.

# -----{ 2.4.4 }-----

Use correct generic for recipe serializer

# -----{ 2.4.3 }-----

Add check if network is optional if it should send the packet to the player

# -----{ 2.4.2 }-----

Add check if network is optional if it should send the packet to the player

# -----{ 2.4.1 }-----

Fix possible crash on neoforge and improve code on fabric

# -----{ 2.4.0 }-----

Deprecated old networking and added a new version.
Removed FinishedCodecRecipe and add CodecRecipeBuilder
Deprecated bound and update render utils to respect that
Added width and height to parent widget
Added number range codecs
Update to 1.20.4
Added nbt validators

# -----{ 2.2.4 }-----

Fixed scroll list scrolling on different axis
Fix neoforge not loading

# -----{ 2.2.3 }-----

Fix neoforge not loading

# -----{ 2.2.2 }-----

Add missing impl on forge

# -----{ 2.2.1 }-----



# -----{ 2.2.0 }-----

1.20.2 Update

# -----{ 2.2.0 }-----

1.20.2 Update

# -----{ 2.1.16 }-----

Added codec metadata section serializer
Added context menus widget

# -----{ 2.1.15 }-----

Added context menus widget

# -----{ 2.1.14 }-----

Fixed issue where hidden widgets could have cursors shown

# -----{ 2.1.13 }-----

Fixed crash in nbt predicates
Added strict mode for nbt predicates

# -----{ 2.1.12 }-----

Added strict mode for nbt predicates
Added ability to set random and get total to WeightedCollection, should allow for more deterministic responses

# -----{ 2.1.11 }-----

Added ability to set random and get total to WeightedCollection, should allow for more deterministic responses
Added Fabric-Loom-Remap to manifest file

# -----{ 2.1.10 }-----

Added Fabric-Loom-Remap to manifest file

# -----{ 2.1.9 }-----

Added component bytecodec and update bytecodec version.
Fixed server translations not pulling value properly.

# -----{ 2.1.8 }-----

Fixed server translations not pulling value properly.

# -----{ 2.1.7 }-----

Added more ByteCodec types

# -----{ 2.1.6 }-----

Added new experimental ByteCodec packets

# -----{ 2.1.5 }-----

Fix CME in creative tabs

# -----{ 2.1.4 }-----

Add dataless packet handler, fix web responses not being correct in WebUtils
Add enumbuilder, fix fabric ingredients, add unsafe methods

# -----{ 2.1.3 }-----

Add enumbuilder, fix fabric ingredients, add unsafe methods

# -----{ 2.1.2 }-----

Add map tag and map collection methods for tags.
Add replace versions of tooltip set methods and add clear tooltip method.
Add mod file paths to mod info and add pretty gson.
Register ingredients properly on forge.
Add CursorWidget and allow for better cursor setting on base screens
Added NetworkChannel#canSendPlayerPackets and NetworkChannel now accept an 'optional' parameter to determine if connections should be refused if channel does not exist on the other side

# -----{ 2.1.1 }-----

Added NetworkChannel#canSendPlayerPackets and NetworkChannel now accept an 'optional' parameter to determine if connections should be refused if channel does not exist on the other side
Breaking change: changed IntContainerData#size to IntContainerData#getSize to fix conflict with yarn mappings

# -----{ 2.1.0 }-----

Breaking change: changed IntContainerData#size to IntContainerData#getSize to fix conflict with yarn mappings
Added ResourcefulCreativeTab#addContent for ability to add a dynamic amount of itemstacks

# -----{ 2.0.8 }-----

Added ResourcefulCreativeTab#addContent for ability to add a dynamic amount of itemstacks
Added support for defining internal resource packs in mods.toml and fabric.mods.json

# -----{ 2.0.7 }-----

Added support for defining internal resource packs in mods.toml and fabric.mods.json

# -----{ 2.0.6 }-----



# -----{ 2.0.5 }-----



# -----{ 2.0.5 }-----

- Full 1.20 release