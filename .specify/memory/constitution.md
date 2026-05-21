<!--
SYNC IMPACT REPORT
==================
Version change: [uninitialized] → 1.0.0
Modified principles: N/A (initial constitution)
Added sections:
  - Core Principles (I–V)
  - Digital & Technical Standards
  - Contribution & Review Workflow
  - Governance
Templates requiring updates:
  - .specify/templates/plan-template.md ✅ (Constitution Check section present, no changes needed)
  - .specify/templates/spec-template.md ✅ (structure compatible)
  - .specify/templates/tasks-template.md ✅ (structure compatible)
Deferred TODOs:
  - TODO(RATIFICATION_DATE): Using today's date as project has no prior recorded ratification.
-->

# Diccionario Muchik Constitution

## Core Principles

### I. Data Fidelity (NON-NEGOTIABLE)

Every lexical entry MUST trace back to a verifiable primary source (colonial-era grammars,
historical manuscripts, ethnographic records, or peer-reviewed linguistic publications).
Unverified reconstructions or inventions are forbidden. When a source is uncertain, the
entry MUST be marked explicitly with a doubt qualifier and the nearest known source cited.
The corpus is finite and irreplaceable — accuracy takes precedence over completeness.

### II. Linguistic Accuracy

Entries MUST follow established conventions for extinct-language documentation:

- Phonetic transcription MUST use IPA notation.
- Each entry MUST declare its grammatical category (noun, verb, particle, etc.).
- Morphological variants (e.g., colonial spelling variants) MUST be cross-referenced rather
  than treated as separate entries.
- Usage examples MUST be drawn from attested corpus when available; constructed examples
  MUST be labelled as such.

### III. Accessibility

The dictionary MUST serve two audiences simultaneously: specialized linguists/researchers
and non-specialist users (students, Lambayeque descendants, general public).

- Technical notation (IPA, grammatical tags) MUST appear alongside plain-language
  explanations in Spanish.
- Navigation and search MUST not require prior linguistic training.
- No feature may be released that degrades readability for non-specialists.

### IV. Full Traceability

Every entry and every editorial decision MUST have a citation chain. This includes:

- Source document title, author, date, and page/folio reference.
- Editor who added or modified the entry and the date of change.
- Rationale for any deviation from the primary source wording.

This principle applies to code artifacts as well: data migrations and schema changes MUST
include a comment explaining which corpus update or editorial policy motivated them.

### V. Incremental Preservation

Partial, correct data is always preferable to delayed, perfect data. Entries MUST be
released as soon as they pass editorial review, without waiting for the full corpus to be
complete. Each release increment MUST be independently coherent (no broken cross-references
or missing source citations). Progress is measured by documented, verified entries — not
by feature count.

## Digital & Technical Standards

### Architecture & Tech Stack (NON-NEGOTIABLE)

The technology stack MUST prioritize a unified codebase with native performance across all platforms (Android, iOS, Web, Desktop).

- **Core:** Kotlin Multiplatform (KMP).
- **UI:** 100% Compose Multiplatform (CMP) across all targets using a Single Shared UI in the `composeApp` module. Platform-specific UI code (`expect/actual`) is strictly limited to hardware access or specific window/inset behaviors.
- **Architecture:** Clean Architecture (Presentation, Domain, Data layers) utilizing the MVVM / MVI pattern for state management.
- **Dependency Injection:** Koin (Multiplatform).
- **Networking:** Ktor client.

### Data Synchronization & Storage

The dictionary MUST function flawlessly in environments with limited connectivity.

- **Offline-First:** All linguistic reads for UI rendering MUST come from a highly performant local database.
- **Local DB:** Room Multiplatform is mandated for local caching and offline availability. Use FTS4/FTS5 virtual tables to power instantaneous, robust search functionality that handles diacritics and special Muchik characters gracefully.
- **Cloud DB:** Supabase (PostgreSQL) is the designated remote source of truth. The app must download and sync the dictionary payload from Supabase to Room upon first launch or manual sync.

### UI/UX & Feature Parity

- No feature from the original HTML prototype may be lost. This includes the static grammar views, the interactive numerical system, and the randomized quiz module.
- The UI MUST maintain the original visual identity (color palette, typography) while implementing native-feeling scroll behaviors, adaptive layouts, and proper safe-area/window-inset handling per platform.
- The search functionality must be highly reactive (implementing debouncing and distinct flows in the ViewModel) and must include an elegant Empty State view.

### Security & Best Practices

- SOLID principles and DRY methodologies are required. UI components (e.g., Word Cards) must be generic and decoupled from business logic.
- API keys and database credentials (e.g., Supabase anon keys) MUST NOT be hardcoded. Use build configurations or environment variables.

## Contribution & Review Workflow

- New entries or edits MUST be proposed via a feature branch following the naming
  convention enforced by the project's branch-numbering policy (sequential).
- Every entry addition or modification MUST be reviewed by at least one other contributor
  before merging; linguistic accuracy review and technical review may be performed by the
  same person if the team is small, but both aspects MUST be explicitly checked.
- Automated checks MUST verify that required fields (headword, grammatical category, source
  citation, IPA) are present before a PR can be merged.
- Breaking changes to the data schema MUST be announced at least one release cycle in
  advance, with a migration guide provided.

## Governance

This constitution supersedes all informal conventions and undocumented practices.
Amendments require:

1. A written proposal describing the change and its rationale.
2. Agreement from the active maintainers (or the project owner if solo).
3. A version bump following semantic versioning (MAJOR for removals/redefinitions,
   MINOR for additions, PATCH for clarifications).
4. An update to this file and propagation to all affected templates.

Compliance is verified at every plan review (Constitution Check gate in plan.md) and at
PR review time. Complexity or deviations from any principle MUST be explicitly justified
in the plan's Complexity Tracking table.

**Version**: 1.0.0 | **Ratified**: 2026-05-20 | **Last Amended**: 2026-05-20
