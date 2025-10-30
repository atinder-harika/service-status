# Contributing to Service Status Monitor

Thank you for wanting to contribute! Follow these simple steps to get started.

> **New here?** Start with the [README](README.md) for project overview, then check [SETUP.md](SETUP.md) to run the project locally.

---

## 🌳 Branching Strategy

We use 3 branches:

- **`development`** ← **Send your PRs here!** (testing branch, nothing deploys)
- **`main`** ← **DO NOT send PRs here!** (production, auto-deploys to live site)

**Workflow:** You contribute → `development` → Maintainer tests locally → `main` (goes live)

---

## 🚀 Contributing Steps

### 1. Fork & Clone
```bash
# Fork on GitHub, then:
git clone https://github.com/YOUR_USERNAME/service-status.git
cd service-status
```

### 2. Create Your Branch
```bash
git checkout development
git pull origin development
git checkout -b feat/your-feature-name
```

### 3. Make Changes
- Code your feature/fix
- Test locally (see [SETUP.md](SETUP.md))
- Run tests: `npm test` (frontend) or `./mvnw test` (backend)

### 4. Commit & Push
```bash
git add .
git commit -m "feat: describe what you did"
git push origin feat/your-feature-name
```

### 5. Open Pull Request
- Go to your fork on GitHub
- Click "New Pull Request"
- **Set base to `development`** (NOT `main`!)
- Fill description, submit

---

## 📝 Branch & Commit Naming

**Branch prefixes:**
- `feat/` - New feature (`feat/add-dark-mode`)
- `fix/` - Bug fix (`fix/timeout-error`)
- `docs/` - Documentation (`docs/update-readme`)
- `test/` - Tests (`test/add-unit-tests`)
- `refactor/` - Code improvement (`refactor/simplify-api`)
- `chore/` - Maintenance (`chore/update-deps`)

**Commit format:** `type: short description`
- ✅ `feat: add auto-refresh toggle`
- ✅ `fix: handle null response`
- ❌ `updated stuff`

---

## ⚠️ CRITICAL: Never Commit Secrets!

**DO NOT commit:**
- ❌ `.env` or `.env.local` files
- ❌ Database passwords
- ❌ API keys or tokens
- ❌ Supabase credentials

**If you accidentally commit a secret:**
1. Tell maintainer immediately
2. Rotate/change the credential
3. Delete the commit if possible

Use `.env.example` files instead (already in repo).

---

## ✅ Pre-Submit Checklist

Before opening your PR:
- [ ] Code works locally
- [ ] Tests pass
- [ ] PR targets `development` (not `main`)
- [ ] Branch name follows naming rules
- [ ] No secrets in code
- [ ] Commit messages are clear

---

## 🔄 What Happens Next?

1. You submit PR to `development`
2. CI tests run automatically
3. Maintainer reviews your code
4. You address feedback (if any)
5. Maintainer merges to `development`
6. Maintainer tests locally
7. When ready, maintainer pushes to `main` (goes live!)

---

## 🤔 Need Help?

- **Setup issues:** [SETUP.md](SETUP.md)
- **Found a bug:** [Open an issue](https://github.com/atinder-harika/service-status/issues)
- **Questions:** Comment on your PR

---

**Thank you for contributing! 🚀**
