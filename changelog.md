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