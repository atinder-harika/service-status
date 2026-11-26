"""Summarize a soak run: heap/threads first vs last sample, and check-cycle time from the logs."""
import csv
import json
import re
import statistics
import sys
from datetime import datetime
from pathlib import Path

out = Path(sys.argv[1])
rows = list(csv.DictReader(open(out / "samples.csv")))
STAMP = re.compile(r"^(\S+T\S+?)\s.*Health check completed for (\S+):")


def cycles(app):
    """Seconds between consecutive completions of the same service = one cycle (interval included)."""
    last, gaps = {}, []
    for line in open(out / f"{app}.log", encoding="utf-8", errors="replace"):
        m = STAMP.match(line)
        if not m:
            continue
        t = datetime.fromisoformat(m.group(1).replace("Z", "+00:00")).timestamp()
        if m.group(2) in last:
            gaps.append(t - last[m.group(2)])
        last[m.group(2)] = t
    return gaps


summary = {}
for app in ("old", "new"):
    s = [r for r in rows if r["app"] == app]
    gaps = cycles(app)
    summary[app] = {
        "minutes": int(s[-1]["t_min"]) if s else 0,
        "heap_mb_first_last": [float(s[0]["heap_used_mb"]), float(s[-1]["heap_used_mb"])] if s else None,
        "heap_mb_max": max(float(r["heap_used_mb"]) for r in s) if s else None,
        "threads_first_last": [int(s[0]["live_threads"]), int(s[-1]["live_threads"])] if s else None,
        "rss_mb_first_last": [float(s[0]["rss_mb"]), float(s[-1]["rss_mb"])] if s else None,
        "median_cycle_s": round(statistics.median(gaps), 1) if gaps else None,
        "checks_completed": sum(1 for line in open(out / f"{app}.log", encoding="utf-8", errors="replace")
                                if "Health check completed" in line),
        "out_of_memory": "OutOfMemoryError" in (out / f"{app}.log").read_text(encoding="utf-8", errors="replace"),
    }
print(json.dumps(summary, indent=2))
(out / "summary.json").write_text(json.dumps(summary, indent=2))
