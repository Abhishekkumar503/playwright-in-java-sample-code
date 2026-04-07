# 📍 GitHub Pages Configuration Guide

## Step-by-Step Setup

### 1. Configure GitHub Pages

**Path**: GitHub Repository → Settings → Pages

**Configuration**:
```
Build and deployment section:
├─ Source: Deploy from a branch
├─ Branch: Allure-Report
├─ Folder: / (root)
└─ Save
```

### 2. Enable Workflows

Ensure these workflows are enabled:
- ✅ Playwright Tests (`.github/workflows/playwright-tests.yml`)
- ✅ Nightly Builds (`.github/workflows/nightly-builds.yml`)
- ✅ Publish Allure Reports (`.github/workflows/static.yml`)

**Path**: GitHub Repository → Actions → Enable workflows (if needed)

---

## ✅ Verification Checklist

### After First Test Run:

1. **Test Execution**
   - [ ] Go to Actions → Playwright Tests
   - [ ] Verify tests completed successfully
   - [ ] Check test count: 6 tests per run

2. **Report Generation**
   - [ ] Go to Actions → Publish Allure Reports
   - [ ] Verify report was generated
   - [ ] Check for artifacts in download section

3. **Branch Creation**
   - [ ] Go to Code → Branches
   - [ ] Verify `Allure-Report` branch exists
   - [ ] Check branch contains report files

4. **GitHub Pages Deployment**
   - [ ] Go to Settings → Pages
   - [ ] Note the URL: `https://<username>.github.io/<repo>/`
   - [ ] Verify "Your site is live at..." message

5. **Access Report**
   - [ ] Open GitHub Pages URL in browser
   - [ ] Verify Allure report displays
   - [ ] Check test results visible

---

## 🔗 Important URLs

### GitHub Actions Workflows
```
https://github.com/<username>/<repo>/actions
```

### Test Results (Artifacts)
```
https://github.com/<username>/<repo>/actions
→ Select workflow run
→ Artifacts section
```

### Allure Report (Public)
```
https://<username>.github.io/<repo>/
```

### Repository Settings
```
https://github.com/<username>/<repo>/settings/pages
```

---

## 📋 Workflow Status Dashboard

### Monitor from GitHub UI:

1. **Recent Runs**
   ```
   Actions tab → All workflows
   └─ Green checkmark = Success
   └─ Red X = Failed
   ```

2. **Test Details**
   ```
   Click workflow run
   └─ See step-by-step logs
   └─ Download artifacts
   ```

3. **Report Status**
   ```
   Actions → Publish Allure Reports
   └─ Last run status
   └─ Deployment status
   ```

---

## 🚨 Troubleshooting

### GitHub Pages Not Showing Report?

**Issue**: Report not displaying or shows 404

**Solutions**:
1. Check branch is set to `Allure-Report` (not `main`)
2. Verify `index.html` exists in Allure-Report branch
3. Wait 2-3 minutes for GitHub Pages to rebuild
4. Clear browser cache (Ctrl+Shift+Delete or Cmd+Shift+Delete)
5. Try incognito/private window

### Workflow Not Triggering?

**Issue**: Nightly build doesn't run at scheduled time

**Solutions**:
1. Workflows must be enabled (default: enabled)
2. Check Actions tab for any disabled workflows
3. Verify cron schedule: `0 0 * * *` = 12 AM UTC
4. Manually trigger via "Run workflow" button

### Tests Failing in GitHub Actions?

**Issue**: Tests pass locally but fail in Actions

**Solutions**:
1. Playwright browsers installed? (Done in workflow)
2. Check timeout settings (60 minutes set)
3. Review test logs for specific errors
4. Verify retry configuration: `rerunFailingTestsCount=2`

---

## 📊 Expected File Structure

### After First Test Run:

```
Repository
├── .github/workflows/
│   ├── playwright-tests.yml      ✅ Active
│   ├── nightly-builds.yml        ✅ Active
│   ├── setup-and-test.yml        ✅ Active
│   └── static.yml                ✅ Active
│
├── src/test/java/
│   └── com/serenitydojo/playwright/GitAction/
│       ├── GitActionPageObject.java
│       ├── GitActionNavigation.java
│       ├── GitActionProductSearch.java
│       ├── GitActionCartActions.java
│       └── GitActionBase.java
│
├── target/
│   ├── allure-results/           ← Test results (JSON)
│   ├── surefire-reports/         ← Maven reports
│   └── site/allure-maven-plugin/ ← Local HTML report
│
├── pom.xml                        ✅ Retry config added
└── (Other branches)
    └── Allure-Report branch       ← Created automatically
        ├── index.html
        ├── data/
        ├── css/
        └── js/
```

---

## 🎯 Success Indicators

You'll know everything is working when you see:

✅ **Workflow Execution**
- Playwright Tests workflow runs (green checkmark)
- Tests complete in ~8-10 seconds
- 6 tests pass per run

✅ **Report Generation**
- Allure Reports workflow starts after tests
- Report generates successfully
- Files push to Allure-Report branch

✅ **GitHub Pages**
- Pages settings shows "Your site is live"
- Report accessible at GitHub Pages URL
- Allure dashboard displays test results

✅ **Scheduled Runs**
- Nightly Builds trigger daily at 12 AM UTC
- Can also trigger manually from Actions UI
- Generates new report each time

---

## 📞 Need Help?

### Common Issues & Fixes:

| Issue | Fix |
|-------|-----|
| Report not deploying | Check GitHub Pages branch setting |
| Tests not running | Enable workflows in Actions settings |
| 404 on GitHub Pages | Clear cache, wait 2-3 minutes |
| Slow test execution | Check browser installation logs |
| Report not updating | Verify Allure-Report branch has new commits |

---

**✨ All set! Your CI/CD pipeline is ready!** 🚀

