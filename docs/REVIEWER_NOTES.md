# Epic ACG 2.2.28 - File review notes

This JAR is an **Epic Fight API compatibility bridge**, not a replacement for the original Epic ACG mod. It uses a standard Java service registration at `META-INF/services/cpw.mods.modlauncher.api.ITransformationService` for a ModLauncher transformation service.

## Why there are embedded replacement classes

Epic ACG 20.12.5.0 was built against older Epic Fight APIs. The bridge must substitute specific classes **in memory** while the game loads; placing those classes at the JAR root would be unsafe because it would create duplicate class definitions. All replacement resources therefore live in `bridge_payload/classes/<fully-qualified-class-name>.class` and are loaded as bytes by the transformer. The names now match the Java classes they contain. They are not encrypted or compressed beyond ordinary JAR ZIP compression. See `docs/CLASS_MANIFEST.json` for SHA-256 checksums and class versions.

The bridge patches classes belonging to Epic ACG, Epic Fight (`yesman.epicfight.skill.SkillContainer`), and a Forge event (`net.minecraftforge.event.AddPackFindersEvent`). The latter provides a runtime Skill Tree resource pack when Skill Tree is installed. All are listed explicitly in the manifest. These substitutions are intentional compatibility operations, not attempts to conceal code.

## Runtime behavior

- Original `epicacg-20.12.5.0-all.jar` remains unchanged on disk and is checked against the expected clean-file SHA-256 `f0314ecd0345ef629759711bac0e438e1eff6d758d8bc6621007e823c80b5504`.
- No network download, native executable, or external code fetching is included in this JAR.
- Mod dependencies: Forge 47.4.20+, Epic Fight 20.14.17, Epic ACG 20.12.5.0. Epic Fight Skill Tree is optional (`mandatory=false` in `mods.toml`).
- Minecraft 1.20.1 uses Java 17; all embedded replacement classes are bytecode major version 61.
- The optional Skill Tree helper is stored under `bridge_payload/epicskills/`.
- Changes from 2.2.27 are **packaging, manifest, documentation, and resource-path updates**, not changes to combat behavior.

## Licensing

The original Epic ACG is credited to DFDYZ and uses GPLv3. The bridge's declared license is GPL-3.0-only; see `LICENSE`. The upstream projects and any redistributed modified classes retain their respective attribution and license obligations. Distributors should also make available the corresponding source for GPL-covered modifications.

## Limitations

This archive has been ZIP-integrity, class-version, and resource-path checked. It has **not** been verified by launching Minecraft. The packaging change does not guarantee acceptance by any automated moderation system.
