# Railway Deployment Quick Reference

## 📝 Pre-Deployment Checklist

- [ ] Railway account created (https://railway.app/)
- [ ] Supabase database credentials ready
- [ ] GitHub repo connected to Railway

## 🚀 Deployment Steps

### 1. Create Railway Project
```
1. Go to: https://railway.app/new
2. Click "Deploy from GitHub repo"
3. Select: atinder-harika/service-status
4. Railway auto-detects monorepo
```

### 2. Add Environment Variables (Railway Dashboard)

**Navigate to:** Your Service → Variables tab

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://YOUR_SUPABASE_HOST:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_supabase_password
SPRING_FLYWAY_SCHEMAS=dev
SPRING_FLYWAY_DEFAULT_SCHEMA=dev
ALLOWED_ORIGINS=https://atinder-harika.github.io
```

### 3. Get Your Railway URL

**Navigate to:** Your Service → Settings → Networking

Click **"Generate Domain"** → Copy URL (e.g., `https://xyz.railway.app`)

### 4. Test Backend

```bash
curl https://YOUR_RAILWAY_URL/actuator/health
```

Expected: `{"status":"UP"}`

### 5. Update GitHub Secret

**Navigate to:** GitHub Repo → Settings → Secrets → Actions

Add new secret:
- **Name:** `BACKEND_URL`
- **Value:** `https://YOUR_RAILWAY_URL`

### 6. Redeploy Frontend (Automatic)

Push to main branch, or manually trigger GitHub Actions workflow.

---

## 🔧 Files Created for Railway

✅ `railway.json` - Railway config (build + start commands)  
✅ `nixpacks.toml` - Nixpacks build config (Java 21)  
✅ `Procfile` - Process definition (backup)  
✅ `RAILWAY_DEPLOY.md` - Full deployment guide  
✅ `backend/src/main/resources/application.properties` - Updated with PORT binding

---

## ✅ Verify Deployment

- [ ] Backend health check passes
- [ ] API endpoint returns data: `curl https://YOUR_RAILWAY_URL/api/services`
- [ ] Frontend loads service data (check browser console)
- [ ] No CORS errors in browser console

---

## 🐛 Common Issues

**"Port not accessible"**
→ Ensure `server.port=${PORT:8080}` in application.properties

**"Database connection failed"**
→ Check Supabase credentials in Railway environment variables

**"Build failed"**
→ Check Railway logs: Dashboard → Deployments → Latest deployment

**"Frontend can't reach backend"**
→ Verify BACKEND_URL GitHub secret is set correctly

---

## 📚 Full Documentation

See `RAILWAY_DEPLOY.md` for detailed step-by-step guide.
