# Railway Deployment Guide - Backend Only

This guide walks you through deploying the **backend only** from this monorepo to Railway.

---

## 🚀 Quick Deploy (5 Minutes)

### Prerequisites
- Railway account (free tier): https://railway.app/
- GitHub repository connected
- Supabase PostgreSQL database credentials

---

## Step 1: Create Railway Project

### Option A: Deploy via Railway Dashboard (Recommended)

1. **Go to Railway Dashboard**
   - Visit: https://railway.app/
   - Click **"New Project"**

2. **Deploy from GitHub Repo**
   - Click **"Deploy from GitHub repo"**
   - Select: `atinder-harika/service-status`
   - Railway will detect it's a monorepo

3. **Configure Root Directory**
   - Railway should auto-detect `backend/` folder
   - If not, manually set:
     - **Root Directory:** `backend`
     - **Build Command:** `./mvnw clean package -DskipTests`
     - **Start Command:** `java -Dserver.port=$PORT -jar target/service-status-backend-0.0.1-SNAPSHOT.jar`

### Option B: Deploy via Railway CLI

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login

# Initialize project in backend directory
cd backend
railway init

# Link to your GitHub repo
railway link

# Deploy
railway up
```

---

## Step 2: Configure Environment Variables

Railway needs your database credentials. Add these in **Variables** tab:

### Required Environment Variables

```bash
# PostgreSQL Connection (from Supabase)
SPRING_DATASOURCE_URL=jdbc:postgresql://YOUR_SUPABASE_HOST:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_supabase_password

# Flyway Schema (dev or prod)
SPRING_FLYWAY_SCHEMAS=dev
SPRING_FLYWAY_DEFAULT_SCHEMA=dev

# Server Port (Railway provides $PORT automatically)
SERVER_PORT=${PORT}

# Production Profile
SPRING_PROFILES_ACTIVE=prod

# CORS Configuration (allow your GitHub Pages frontend)
ALLOWED_ORIGINS=https://atinder-harika.github.io
```

### How to Add Variables in Railway:

1. Go to your Railway project dashboard
2. Click on your service (e.g., `service-status-backend`)
3. Go to **"Variables"** tab
4. Click **"+ New Variable"**
5. Add each variable above

---

## Step 3: Railway Configuration Files (Already Created!)

I've created three config files at the root of your repo:

### 1. `railway.json` (Railway-specific config)
```json
{
  "build": {
    "builder": "NIXPACKS",
    "buildCommand": "cd backend && ./mvnw clean package -DskipTests"
  },
  "deploy": {
    "startCommand": "cd backend && java -Dserver.port=$PORT -jar target/*.jar"
  }
}
```

### 2. `nixpacks.toml` (Build configuration)
```toml
[phases.setup]
nixPkgs = ["jdk21"]

[phases.build]
cmds = ["cd backend && ./mvnw clean package -DskipTests"]

[phases.start]
cmd = "cd backend && java -Dserver.port=$PORT -jar target/*.jar"
```

### 3. `Procfile` (Process definition - backup for Railway)
```
web: cd backend && java -Dserver.port=$PORT -jar target/service-status-backend-0.0.1-SNAPSHOT.jar
```

---

## Step 4: Deploy!

### Via Dashboard:
- Railway auto-deploys when you push to `main` branch
- Click **"Deploy"** button to manually trigger

### Via CLI:
```bash
cd backend
railway up
```

### Check Deployment Logs:
- Go to Railway dashboard → your service → **"Deployments"** tab
- Click on latest deployment to see build/runtime logs

---

## Step 5: Get Your Backend URL

After deployment succeeds:

1. Go to **"Settings"** tab in Railway dashboard
2. Click **"Generate Domain"** under **"Networking"**
3. Railway will give you a URL like:
   ```
   https://service-status-backend-production.up.railway.app
   ```

4. **Test the endpoint:**
   ```bash
   curl https://YOUR_RAILWAY_URL/actuator/health
   ```

   Expected response:
   ```json
   {
     "status": "UP"
   }
   ```

---

## Step 6: Update Frontend to Use Railway Backend

### Add GitHub Secret for CI/CD:

1. Go to GitHub repo: `Settings` → `Secrets and variables` → `Actions`
2. Click **"New repository secret"**
3. Add:
   - **Name:** `BACKEND_URL`
   - **Value:** `https://YOUR_RAILWAY_URL` (without trailing slash)

### Or Update Local Frontend Config:

Edit `frontend/.env.local`:
```env
VITE_API_URL=https://YOUR_RAILWAY_URL
```

Then rebuild frontend:
```bash
cd frontend
npm run build
npm run deploy  # Redeploy to GitHub Pages
```

---

## Monorepo Troubleshooting

### ❌ Problem: Railway builds from root, not backend/

**Solution 1:** Use `railway.json` (already created)

**Solution 2:** Set Root Directory manually:
- Railway Dashboard → Service → Settings → "Root Directory" → `backend`

### ❌ Problem: Build fails with "mvnw: Permission denied"

**Solution:** Railway should auto-detect this, but if needed, add to `nixpacks.toml`:
```toml
[phases.setup]
cmds = ["chmod +x backend/mvnw"]
```

### ❌ Problem: "Port 8080 not accessible"

**Solution:** Ensure your Spring Boot app uses `$PORT` env var:

Add to `backend/src/main/resources/application.properties`:
```properties
server.port=${PORT:8080}
```

This means: Use `$PORT` if set by Railway, else default to 8080 locally.

### ❌ Problem: Database connection fails

**Solution:** Double-check Supabase credentials:
1. Go to Supabase → Project Settings → Database
2. Copy **Connection String** (Transaction pooler mode)
3. Replace `[YOUR-PASSWORD]` with actual password
4. Update Railway environment variables

---

## Railway Free Tier Limits

- ✅ **$5 free credit/month** (about 500 hours of uptime)
- ✅ **Automatic HTTPS**
- ✅ **Custom domain support**
- ⚠️ **App sleeps after inactivity** (15-30 min wake-up time)
- ⚠️ **Limited to 512MB RAM, 1 vCPU**

### Keepalive Strategy (Optional):

Use GitHub Actions to ping your backend every 10-14 minutes:

Create `.github/workflows/keepalive-railway.yml`:
```yaml
name: keepalive-railway

on:
  schedule:
    - cron: '*/12 * * * *'  # Every 12 minutes
  workflow_dispatch:  # Manual trigger

jobs:
  ping:
    runs-on: ubuntu-latest
    steps:
      - name: Ping Railway Backend
        run: |
          curl -f https://YOUR_RAILWAY_URL/actuator/health || echo "Backend is waking up..."
```

---

## Deployment Checklist

- [ ] Railway project created
- [ ] GitHub repo connected
- [ ] Environment variables set (DATABASE_URL, etc.)
- [ ] `railway.json`, `nixpacks.toml`, `Procfile` committed
- [ ] First deployment successful
- [ ] Backend URL obtained (e.g., `https://xyz.railway.app`)
- [ ] Backend health check passes: `curl https://YOUR_URL/actuator/health`
- [ ] BACKEND_URL GitHub secret added
- [ ] Frontend redeployed with new backend URL
- [ ] End-to-end test: Frontend → Backend → Database

---

## Verify End-to-End

1. **Backend health:** `curl https://YOUR_RAILWAY_URL/actuator/health`
2. **API endpoint:** `curl https://YOUR_RAILWAY_URL/api/services`
3. **Frontend:** Visit `https://atinder-harika.github.io/service-status/`
4. **Check console:** Should see service data loading (no errors)

---

## Update Documentation

After successful deployment, update:

### 1. `README.md` - Add Railway URL:
```markdown
## 🚀 Live Demo

- **Frontend:** https://atinder-harika.github.io/service-status/
- **Backend API:** https://YOUR_RAILWAY_URL
```

### 2. `SETUP.md` - Add Railway deployment section

---

## Common Railway Commands

```bash
# View logs
railway logs

# Open dashboard
railway open

# Check status
railway status

# Redeploy
railway up --detach

# Environment variables
railway variables
```

---

## Need Help?

- **Railway Docs:** https://docs.railway.app/
- **Railway Discord:** https://discord.gg/railway
- **This repo's issues:** https://github.com/atinder-harika/service-status/issues

---

## Next Steps After Deployment

1. ✅ Test all API endpoints
2. ✅ Verify WebSocket connections (when implemented)
3. ✅ Set up keepalive cron job
4. ✅ Monitor Railway usage dashboard
5. ✅ Update README.md with live URLs
6. ✅ Tag release: `v1.0.0` (first production deployment!)

**Congratulations! Your full-stack app is now live!** 🎉
