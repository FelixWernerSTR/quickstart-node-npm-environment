Dieses Maven-Projekt installiert Node in bestimmten Version(${node.version}) um dann in der Shell verwenden zu können(startConsoleWithNodeNpmSupport.cmd).

.npmrcCopyToUserProfile sollte umbenannt(.npmrc) angepasst und nach C:\Users\[USERNAME] kopiert werden. Macht man das nicht bleibt der Maven-Build hängen.

Durch den Build wird auch getestet: 
1. ob die bestimmte Node-Version auch in unserem Nexus in der Raw-Repository korrekt hochgeladen wurde und funktional ist. 
Node kann im Terminal/Console verwendet werden(Aufruf erfolgreich?: npm cache clean --force). Ein Jenkins-Job mit gleichem Namen existiert.
2. Ein Set von NPM-Modulen auch abrufbar/abholbar über unseren Nexus ist. Siehe package.json bzw. nach dem Build erzeugte package-lock.json
3. Das Projekt kann als Vorlage benutzt werden um ein Vue/Angular/React-Projekt mit Maven automatisiert und symmetrisch(Workstation/Jenkins) zu bauen. 

Optional: jhipster-sample-setup siehe entsprechend das gleichnamige Maven-Profil
