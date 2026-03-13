#!/usr/bin/bash

export JDTLS_JAVA_SELECTOR='[
{ "scheme": "file", "language": "java", "pattern": "**/*.dsl" },
{ "scheme": "file", "language": "java", "pattern": "**/*.java" },
{ "scheme": "jdt", "language": "java", "pattern": "**/*.class" },
{ "scheme": "untitled", "language": "java", "pattern": "**/*.java" }
]'

export JDTLS_DOCUMENT_SELECTOR='[
{ "scheme": "file", "language": "dsl" },
{ "scheme": "file", "language": "java" },
{ "scheme": "jdt", "language": "java" },
{ "scheme": "untitled", "language": "java" },
{ "pattern": "**/pom.xml" },
{ "pattern": "**/{build,settings}.gradle" },
{ "pattern": "**/{build,settings}.gradle.kts" }
]'

export JDTLS_CLIENT_DOCUMENT_SELECTOR='[
{ "scheme": "file", "language": "dsl" },
{ "scheme": "file", "language": "java" },
{ "scheme": "jdt", "language": "java" },
{ "scheme": "untitled", "language": "java" },
{ "scheme": "vscode-notebook-cell", "language": "java" }
]'

export JDTLS_CLIENT_SYNCHRONIZE='[
{ "scheme": "file", "language": "dsl" },
{ "scheme": "file", "language": "java" },
{ "scheme": "jdt", "language":"java" },
{ "scheme": "untitled", "language": "java" },
{ "scheme": "vscode-notebook-cell" , "language": "java" }
]'

export JDTLS_ECLIPSE_APPLICATION='org.example.mydsl.DslLanguageServerApplication'

export JDTLS_ECLIPSE_PRODUCT='org.example.mydsl.support.product'

export JDTLS_WORKSPACE_PATH='/data/tmp/jdtls_dsl/'

export JDT_LS_PATH='/data/git/redhat/eclipse.jdt.ls/org.eclipse.jdt.ls.product/target/repository/'

VSCODE_INSTALL='/data/vscode/VSCode-linux-x64-1772846543'

VSCODE_HOME="${VSCODE_INSTALL}-test"

truncate -s 0 "${JDTLS_WORKSPACE_PATH}/.metadata/.log"
$VSCODE_HOME/bin/code "${JDTLS_WORKSPACE_PATH}/TestDsl"
