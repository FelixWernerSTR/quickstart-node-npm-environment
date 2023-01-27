Dieses Maven-Projekt installiert Node in bestimmten Version(${node.version}) um dann in der Shell verwenden zu können(startConsoleWithNodeNpmSupport.cmd).

.npmrcCopyToUserProfile sollte umbenannt(.npmrc) angepasst und nach C:\Users\[USERNAME] kopiert werden

Durch den Build wird auch getestet: 
1. ob die bestimmte Node-Version funktional ist.
2. Ein Set von NPM-Modulen abrufbar/abholbar ist. Siehe package.json bzw. nach dem Build erzeugte package-lock.json

Optional: jhipster-sample-setup siehe entsprechend das gleichnamige Maven-Profil und das JDL-Model sample.jd im Ordner jhipster-sample-setup.


