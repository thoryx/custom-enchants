# CustomEnchants (Spigot/Paper plugin)

Adds three custom enchantments for Minecraft 1.20+: Lightning Edge, Lifesteal, Shadow Step.


Build & Install

1. Ensure Java 17 and Maven are installed.
2. You can use the included PowerShell helper to build the plugin and optionally copy the JAR to your server `plugins` folder.

Build only:

```powershell
.\build.ps1
```

Build and copy to server (example — replace path):

```powershell
.\build.ps1 -CopyTo 'C:\path\to\paper-server\plugins'
```

Or run maven manually:

```powershell
mvn -DskipTests package
```

3. After build the JAR is in `target/` (e.g. `target/custom-enchants-1.0.0.jar`). Copy it to your server `plugins/` folder and restart the server.

Notes
- The plugin registers custom enchantments using `NamespacedKey`.
- Cooldowns: Lightning Edge (per-target 5s), Shadow Step (10s).
- Lifesteal caps at player's max health and will not overheal.

Commands
- `/giveenchant <lightning|lifesteal|shadow> [level]` — applies the chosen custom enchant to the item in your main hand. Requires `op` by default.

CI Build (I can build the JAR for you)
- If you don't want to run Maven locally I added a GitHub Actions workflow that builds the plugin and uploads the JAR as a workflow artifact.
- Steps:
	1. Create a new GitHub repo and push this project (all files) to it.
	2. Open the repository on GitHub, go to the `Actions` tab and run the `Build and upload plugin JAR` workflow (or wait for a push to `main`).
	3. When the workflow finishes, open the workflow run and download the artifact named `custom-enchants-jar` — that ZIP contains the built JAR in `target/`.

If you want, I can also add a small GitHub Actions release workflow to attach the JAR to releases automatically.
