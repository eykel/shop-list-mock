# Shop List Mock

Product catalog app built with **Kotlin Multiplatform + Compose Multiplatform**, targeting
Android (verified) and iOS (shared module builds for iOS targets; no Xcode host was scaffolded
from this Windows machine).

This repo is a portfolio piece: it exists to show *how* a small feature gets built, not just
the end state — every layer (network → repository → use case → ViewModel/MVI → UI) landed as
its own branch and pull request. See the closed PRs for the incremental history.

## What it does

- **List screen** — browses a product catalog (grid, loading/empty/error states, pull-to-retry).
- **Detail** — tapping a product opens its detail in a bottom sheet (image, price, rating,
  description, stock).

## The "mock backend"

There is no real server. Instead, `core/network/` wires a real `HttpClient` (Ktor) through the
same pipeline a production app would use — content negotiation, kotlinx.serialization, typed
error mapping — but the engine is `MockEngine`, which intercepts requests by URL and answers
with local JSON fixtures (with simulated latency and an injectable failure rate). Swapping it
for `OkHttp`/`Darwin` at the DI boundary is a one-line change; nothing above the client knows
the difference. That's the point: the layering is real, only the wire is fake.

## Architecture

Progress and structure will be documented here as the layers land (see open/closed PRs).
