#!/bin/bash
COMMIT_MESSAGE=${1:?"Please provide commit message"}
cp ./target/lightfish.war ~/work/workspaces/javaee-patterns-hg/lightfish
cd ~/work/workspaces/javaee-patterns-hg/lightfish
hg add .
hg commit -m"${COMMIT_MESSAGE}"
