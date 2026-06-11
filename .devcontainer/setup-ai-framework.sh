#!/bin/bash
set -e

echo "========================================================="
echo "  Initializing Cloud Environment & Linking AI Framework  "
echo "========================================================="

# 1. Navigate to the generic workspaces directory root
# Codespaces clones your main repo under /workspaces/solar-system-monitoring
cd /workspaces

# 2. Clone the companion ai-framework repository alongside the codebase
if [ ! -d "ai-framework" ]; then
    echo "--> Cloning ai-framework repository..."
    git clone https://github.com/b-asaf/ai-framework.git ai-framework
else
    echo "--> ai-framework already exists locally, fetching latest changes..."
    cd ai-framework && git pull && cd /workspaces
fi

# 3. Connect the .opencode agent infrastructure to the target codebase
echo "--> Syncing agent guidelines and orchestration constraints..."
cd /workspaces/solar-system-monitoring

# Symlink the framework's .opencode configurations so local tools/CLI find them
if [ -d "../ai-framework/.opencode" ]; then
    rm -rf .opencode
    ln -s /workspaces/ai-framework/.opencode .opencode
    echo "--> Successfully linked .opencode to active workspace."
fi

# 4. Install target code base workspace dependencies
echo "--> Running target project environment package installations..."
npm install

echo "========================================================="
echo "              Workspace Setup Complete!                  "
echo "========================================================="