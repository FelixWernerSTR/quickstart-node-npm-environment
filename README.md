Dieses Maven-Projekt installiert Node in bestimmten Version(${node.version}) um dann in der Shell verwenden zu können(startConsoleWithNodeNpmSupport.cmd).

.npmrcCopyToUserProfile sollte umbenannt(.npmrc) angepasst und nach C:\Users\[USERNAME] kopiert werden. Macht man das nicht bleibt der Maven-Build hängen.

Durch den Build wird getestet: 
1. bestimmte Node-Version ist in unserem Nexus in(Raw-Repository) korrekt hochgeladen und ist funktional. 
Node kann im Terminal/Konsole verwendet werden(Aufruf erfolgreich?: npm cache clean --force).
2. ein Set von NPM-Modulen abrufbar/abholbar über unseren Nexus. Siehe package.json bzw. nach dem Build erzeugte package-lock.json
3. das Projekt kann als Vorlage benutzt werden um ein Vue/Angular/React-Projekt mit Maven automatisiert symmetrisch zu Workstation<->Jenkins zu bauen. 

Optional: "jhipster-setup/jhipster-setup-quarkus" siehe entsprechend das gleichnamige Maven-Profil
