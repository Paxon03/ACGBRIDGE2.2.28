Epic ACG - Epic Fight 20.14.17 Compatibility Bridge 2.2.28
Author: Paxon_03 | Original Epic ACG author: DFDYZ

Purpose
-------
Run the unmodified Epic ACG 20.12.5.0 with Epic Fight 20.14.17
on Minecraft Forge 1.20.1 / Forge 47.4.20+ / Java 17.

Changes from 2.2.27
-------------------
- Renamed the runtime replacement-class resources from anonymous numbered
  .bin files to their real fully-qualified class names with .class suffix.
- The replacement classes are still stored under bridge_payload/ and are
  NOT loaded as ordinary top-level Java classes; ModLauncher reads them
  as resources and applies them only at runtime.
- Added explicit class manifest with SHA-256 digests and a description
  of each replacement target, plus reviewer-oriented documentation.
- Corrected stale README and Java-21-looking build manifest metadata.
- Aligned declared licensing with the original Epic ACG GPLv3 license.
- No intended combat, recipe, moveset, skill, or texture changes.

Security / transparency
-----------------------
This is a runtime bytecode compatibility bridge, not a standalone fork.
It does not overwrite or modify the original Epic ACG JAR on disk.
It checks the clean original ACG file's SHA-256 before applying patches.
It uses a ModLauncher ITransformationService to replace incompatible
classes in memory. The exact class names and checksums are in
bridge_payload/classes/index.txt and docs/CLASS_MANIFEST.json.

See docs/REVIEWER_NOTES.md for inspection details.
This build is structurally verified, not Minecraft-runtime tested.
