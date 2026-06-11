#!/bin/bash
set -e

echo "========================================================="
echo "  Initializing Cloud Environment & Linking AI Framework  "
echo "========================================================="

cd /workspaces

# 1. Clone or update the framework repository alongside the product repo
if [ ! -d "ai-framework" ]; then
    echo "--> Cloning ai-framework repository..."
    git clone https://github.com/b-asaf/ai-framework.git ai-framework
else
    echo "--> ai-framework already exists locally, fetching updates..."
    cd ai-framework && git pull && cd /workspaces
fi

# 2. Setup directory linking
cd /workspaces/solar-system-monitoring
if [ -d "../ai-framework/.opencode" ]; then
    rm -rf .opencode
    ln -s /workspaces/ai-framework/.opencode .opencode
    echo "--> Successfully linked .opencode to active workspace."
fi

# 3. 🛡️ INJECT THE LOCAL GIT HOOK (This solves the missing hook issue)
echo "--> Injecting local git branch guardrails..."
cat << 'EOF' > .git/hooks/pre-push
#!/bin/bash
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" = "main" ]; then
    echo ""
    echo "====================================================================="
    echo " [CRITICAL ERROR] AI Agent execution halted: Direct pushes to 'main' are forbidden."
    echo " Please checkout a feature branch and submit a manual-review Pull Request."
    echo "====================================================================="
    echo ""
    exit 1
fi
EOF

# Give the hook execution permissions inside the container
chmod +x .git/hooks/pre-push

# 4. Install project dependencies
echo "--> Running target project environment package installations..."
npm install

echo "========================================================="
echo "              Workspace Setup Complete!                  "
echo "========================================================="