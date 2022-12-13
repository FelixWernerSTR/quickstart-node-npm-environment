Dieses Maven-Projekt installiert Node in bestimmten Version(${node.version}) um dann in der Shell verwenden zu können(startConsoleWithNodeNpmSupport.cmd).

.npmrcCopyToUserProfile sollte umbenannt(.npmrc) angepasst und nach C:\Users\[USERNAME] kopiert werden

Durch den Build wird auch getestet: 
1. ob die bestimmte Node-Version auch in unserem Nexus in der Raw-Repository korrekt hochgeladen wurde und funktional ist. Ein Jenkins-Job mit gleichem Namen existiert.
2. Ein Set von NPM-Modulen auch abrufbar/abholbar über unseren Nexus ist. Siehe package.json bzw. nach dem Build erzeugte package-lock.json

Optional: jhipster-sample-setup siehe entsprechend das gleichnamige Maven-Profil


