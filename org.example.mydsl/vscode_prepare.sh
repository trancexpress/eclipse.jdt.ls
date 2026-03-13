#!/usr/bin/bash

VSCODE_JAVA='/data/git/redhat/vscode-java/'
ECLIPSE_JDTLS='/data/git/redhat/eclipse.jdt.ls/'

VSCODE_INSTALL='/data/vscode/VSCode-linux-x64-1772846543'

VSCODE_HOME="${VSCODE_INSTALL}-test"
rm -rf "${VSCODE_HOME}"
cp -rf "${VSCODE_INSTALL}" "${VSCODE_HOME}"
mkdir -p "${VSCODE_HOME}/data/"

(
cd "${ECLIPSE_JDTLS}"
JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./mvnw -P update-site clean verify -U -DskipTests=true
)

(
cd "${VSCODE_JAVA}"
npm install
vsce package
)

${VSCODE_HOME}/bin/code --install-extension ${VSCODE_JAVA}/java-*.vsix
