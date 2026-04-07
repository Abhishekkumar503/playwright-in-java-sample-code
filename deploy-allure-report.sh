#!/bin/bash
# Script to push Allure report to Allure-Report branch for GitHub Pages deployment

set -e

echo "🚀 Setting up Allure Report deployment..."

# Check if allure results exist
if [ ! -d "target/allure-results" ]; then
    echo "❌ No allure-results directory found!"
    exit 1
fi

echo "✅ Allure results found"

# Install Allure if not present
if ! command -v allure &> /dev/null; then
    echo "📦 Installing Allure Command Line..."
    npm install -g allure-commandline
fi

# Generate Allure report
echo "📊 Generating Allure report..."
allure generate target/allure-results --clean -o target/allure-report

echo "✅ Report generated successfully"

# Create or checkout Allure-Report branch
echo "🌿 Setting up Allure-Report branch..."
git config user.email "automation@github.com"
git config user.name "GitHub Actions"

# Check if remote branch exists
if git rev-parse --verify origin/Allure-Report > /dev/null 2>&1; then
    echo "✅ Allure-Report branch exists, checking it out..."
    git fetch origin Allure-Report
    git checkout Allure-Report
else
    echo "📝 Creating new Allure-Report branch..."
    git checkout --orphan Allure-Report
    git rm -rf .
fi

# Copy report files
echo "📁 Copying report files..."
cp -r target/allure-report/* .

# Clean up unnecessary files
rm -f .gitignore .github/workflows/* 2>/dev/null || true
echo "target/" > .gitignore

# Commit and push
echo "💾 Committing changes..."
git add -A
git commit -m "Update Allure Report - $(date '+%Y-%m-%d %H:%M:%S')" || echo "No changes to commit"

echo "📤 Pushing to Allure-Report branch..."
git push origin Allure-Report

echo "✅ Allure Report successfully deployed to Allure-Report branch!"
echo "📍 GitHub Pages URL: https://${{ github.repository_owner }}.github.io/${{ github.event.repository.name }}/"

