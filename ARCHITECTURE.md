# 📘 KMP + Compose Multiplatform Architecture Guide
> **Version:** 2.0 | **Stack:** Kotlin Multiplatform + Compose + Metro + Navigation 3 + Kotlin DSL  
> **Core Principles:** Cross-platform UI, Readability, Performance, Clean Boundaries  
> **Target Audience:** Developers, Code Reviewers, LLM Agents

---

## 🔑 Quick Reference for LLM Agents & Humans
| Rule                    | Implementation                                                                 |
|-------------------------|--------------------------------------------------------------------------------|
| **Architecture**        | Clean Architecture + MVVM                                                      |
| **State Flow**          | `mutableStateOf` / `mutableStateListOf` in ViewModel → `by collectAsState()` in Compose |
| **Async**               | `viewModelScope.launch` + `Flow` pipelines                                     |
| **Error/Loading**       | Compose `mutableStateOf` / `mutableStateListOf` in ViewModel (UI layer only)   |
| **DI**                  | Metro https://github.com/ZacSweers/metro        |
| **Navigation**          | Navigation 3 https://developer.android.com/guide/navigation/navigation-3 |
| **Package Root**        | `com.example.app`                                                              |
| **UI Packages**         | `com.example.app.ui.{feature}`                                                 |
| **Domain Packages**     | `com.example.app.domain.{feature}`                                             |
| **Repository Packages** | `com.example.app.data.{feature}`                                               |
| **Base Packages**       | `com.example.app.entity`                       |
| **Build**               | Kotlin DSL + Version Catalog (`libs.versions.toml`)                            |
| **Platform Code**       | `expect/actual` only for system APIs, never for UI                             |

---

## 1. Architecture Overview
- **Pattern:** Clean Architecture layered with MVVM
- **Flow:** `UI (Compose) → ViewModel → *Service → Repository (direct impl) → Remote/Local`
- **Module Structure:** Monolithic `commonMain` with feature-based packages
- **UI Sharing:** 100% shared. Platform differences handled via `expect/actual` for system APIs (network, storage, sensors), never for Compose components
- **State Management:** Compose `mutableStateOf` in ViewModel, collected via `collectAsState()` in Compose
- **DI Framework:** Metro (compile-time, `@DependencyGraph` interface)
- **Navigation:** Navigation 3 (stable 1.1, typed `NavKey` routes, user-owned back stack `SnapshotStateList`, `NavDisplay`)
- **Nav KMP Note:** Non-JVM platforms require `SavedStateConfiguration` with `SerializersModule` for polymorphic `NavKey` serialization

---

## 2. Package Structure & Conventions
```
commonMain/kotlin/com/example/app/
├── ui/                          # ← Shared UI layer
│   ├── home/                    # ← Feature: screens, components, ViewModel
│   │   ├── HomeScreen.kt
│   │   ├── HomeComponents.kt
│   │   └── HomeViewModel.kt
│   ├── common/                  # ← Shared UI utilities, themes, router
│   └── theme/                   # ← Material3, ColorScheme, Typography
├── domain/                      # ← Business logic
│   ├── auth/
│   │   ├── AuthService.kt       # ← [Feature]Service
│   └── core/                    # ← Base interfaces, Result, Constants
├── data/                        # ← Infrastructure: API, DB, Cache, Mappers
│   ├── auth/
│   │   ├── AuthRemoteRepository.kt
│   │   ├── AuthLocalRepository.kt
│   │   └── AuthRepository.kt
├── Entity.kt                     # ← Base data classes, DTOs, Entities
├── DI.kt                         # ← DI modules
└── App.kt                        # ← entry point for platforms
```
**Rules:**
- Each feature lives in `ui.{feature}`
- Feature-specific components stay in the same package
- Domain contains services only (repository interfaces are unnecessary for single implementations)
- Data contains direct repository implementations injected via `@Inject`
- `Entity` holds platform-agnostic data models
- `DI` holds DI modules and factories

---

## 3. Layer Responsibilities & Data Flow
```
[UI Layer] Compose → ViewModel (mutableStateOf) → [Domain Layer] Service → [Data Layer] Repository → Remote/Local
```
- **UI:** Pure Compose. No business logic. Only observes state and handles user input.
- **ViewModel:** Manages UI state using Compose `mutableStateOf` / `mutableStateListOf`, coordinates domain calls, exposes state via `Flow<T>` for reactive streams.
- **Domain:** Contains `*Service` (business operations). Framework-agnostic.
- **Data:** Direct repository implementations (injected via `@Inject`), handles remote/local fetching, caching, and mapping to `entity/`.

**Repository Example (direct implementation):**
```kotlin
// data/auth/AuthRepository.kt
class AuthRepository @Inject constructor(
    private val remote: AuthRemoteRepository,
    private val local: AuthLocalRepository
) {
    suspend fun getProfile(): Result<UserProfile> = local.getProfile()
        ?: remote.getProfile().also { local.save(it) }
}
```

**Rules:**
- When a repository has a single implementation, skip the interface entirely. Inject the class directly via `@Inject` into the Service.
- Only introduce an interface when there are multiple implementations (e.g., platform-specific behavior, mock for testing, or feature flags).

---

## 4. State Management & Async Handling

### ViewModel State: Compose Mutable States (UI Layer Only)

ViewModels use Compose's `mutableStateOf` / `mutableStateListOf` for UI state. This keeps state composition-aware and eliminates the boilerplate of `sealed class UiState<Loading, Success, Error>`.

```kotlin
class HomeViewModel @Inject constructor(
    private val homeService: HomeService,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var data by mutableStateOf<List<HomeData>>(emptyList())
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                data = homeService.fetchData()
            } catch (e: Throwable) {
                error = e.message ?: "Unknown"
            } finally {
                isLoading = false
            }
        }
    }
}
```

**Rules:**
- Use `mutableStateOf` / `mutableStateListOf` for all UI state in ViewModels (UI layer only).
- Domain services and data layer continue to use `Flow` / `StateFlow` for reactive streams.
- Expose individual state properties (`isLoading`, `data`, `error`) rather than a sealed class wrapper.
- Use `private set` to prevent external mutation.
- For complex streaming data (e.g., real-time updates), expose a `Flow<T>` directly and collect it in Compose.

### Compose Consumption
```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    if (viewModel.isLoading) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    } else if (viewModel.error != null) {
        ErrorView(viewModel.error!!, onRetry = { viewModel.loadItems() })
    } else {
        HomeContent(viewModel.data)
    }
}
```

### Flow-Based State (for real-time / continuous streams)
```kotlin
@Composable
fun QuestionScreen(viewModel: OpenQuestionViewModel = viewModel()) {
    val feedback by viewModel.feedback.collectAsState()
    // ...
}
```

---

## 5. UI & Compose Guidelines
- **Component Strategy:** Hybrid. Large feature blocks (`HomeScreen`, `ProfileCard`) + small reusable widgets (`PrimaryButton`, `LoadingSpinner`)
- **Theming:** Shared `MaterialTheme` across all platforms. Dynamic colors via `ColorScheme`. Platform-specific typography/colors injected via `expect/actual` only if strictly necessary.
- **Performance:**
    - Wrap expensive calculations in `remember` or `derivedStateOf`
    - Use `key` on lazy lists
    - Avoid passing large objects by value in `@Composable` parameters
- **Runtime:** `androidx.lifecycle:lifecycle-viewmodel-compose` is mandatory for lifecycle-aware collection.

---

## 6. Navigation & Dependency Injection
### Metro DI

Metro is a compile-time DI framework using a Kotlin compiler plugin (no KAPT/KSP). Graphs are interfaces annotated with `@DependencyGraph`.

#### Graph Setup

```kotlin
@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
internal interface AppGraph {
    @SingleIn(AppScope::class)
    @Provides
    fun provideJson(): Json = Json { ... }

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClient(json: Json): HttpClient = HttpClient { ... }

    @DependencyGraph.Factory
    interface Factory {
        fun create(clientSpellcheck: ClientSpellcheck): AppGraph
    }
}
```

#### Bootstrapping in Compose

```kotlin
@Composable
internal fun WithAppGraph(
    clientSpellcheck: ClientSpellcheck,
    content: @Composable () -> Unit,
) {
    val graph = remember {
        createGraphFactory<AppGraph.Factory>().create(clientSpellcheck)
    }
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory
    ) {
        content()
    }
}
```

#### Registering Dependencies

**Direct injection (single implementation):**
```kotlin
@Inject
@SingleIn(AppScope::class)
class DataService(private val httpClient: HttpClient) { ... }
```

**Interface binding (multiple implementations):**
```kotlin
interface ClientSpellcheck {
    suspend fun correct(word: String): Boolean
}

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
internal class MySpellcheck : ClientSpellcheck { ... }
```

#### ViewModel Creation & Injection

Metro provides two ways to create ViewModels in Compose, depending on whether the ViewModel needs constructor parameters.

**Bootstrap at app entry point** — call `WithAppGraph()` before navigation setup:
```kotlin
@Composable
fun App() {
    WithAppGraph(clientSpellcheck = localSpellcheck) {
        // navigation, screens, etc.
    }
}
```

**Pattern A: Non-assisted ViewModels** (no constructor parameters needed)

Register with `@ContributesIntoMap` + `@ViewModelKey`:
```kotlin
@ContributesIntoMap(AppScope::class)
@ViewModelKey(WelcomeViewModel::class)
@Inject
class WelcomeViewModel(
    private val dataService: DataService
) : ViewModel() {
    var isLoading by mutableStateOf(false) private set
    var items by mutableStateOf<List<LessonHeader>>(emptyList()) private set
    var error by mutableStateOf<String?>(null) private set
    // ...
}
```

Inject in Compose via `metroViewModel<T>()`:
```kotlin
@Composable
fun WelcomePage(onLessonClick: (LessonHeader) -> Unit) {
    val vm = metroViewModel<WelcomeViewModel>()
    // observe vm.isLoading, vm.items, vm.error
}
```

**Pattern B: Assisted ViewModels** (need per-instance parameters like `id: String`)

Register with `@AssistedInject` + `@AssistedFactory`:
```kotlin
@AssistedInject
class LessonViewModel(
    @Assisted private val id: String,
    private val dataService: DataService
) : ViewModel() {
    // ...
    
    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(id: String): LessonViewModel
    }
}
```

Inject in Compose via `assistedMetroViewModel<T, Factory>(key = id) { create(id) }`:
```kotlin
@Composable
fun LessonPage(id: String, onBack: () -> Unit) {
    val vm = assistedMetroViewModel<LessonViewModel, LessonViewModel.Factory>(key = id) {
        create(id)
    }
    // observe vm.isLoading, vm.lesson, vm.error
}
```

**Navigation integration** — tie ViewModel lifecycle to navigation entries:
```kotlin
NavDisplay(
    backStack = backStack,
    entryDecorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()  // ViewModel survives config changes, destroyed on pop
    ),
    entryProvider = entryProvider {
        entry<WelcomeScreen> { WelcomePage(...) }
        entry<LessonScreen> { LessonPage(id = it.id, onBack = it::pop) }
    }
)
```

**Rules:**
- DI graphs reside in `commonMain` or platform entry points
- Use `@DependencyGraph` interface with `@Provides` functions or `@ContributesBinding`
- Use `@SingleIn(AppScope::class)` for scoped/singleton bindings
- Use `createGraphFactory<Factory>().create()` to instantiate
- Non-assisted ViewModels: register with `@ContributesIntoMap` + `@ViewModelKey`, inject via `metroViewModel<T>()`
- Assisted ViewModels: use `@AssistedInject` + `@AssistedFactory`, inject via `assistedMetroViewModel<T, Factory>(key = id) { create(id) }`
- Always use `rememberViewModelStoreNavEntryDecorator()` to tie ViewModel lifecycle to navigation entries
- Use `@ContributesTo(AppScope::class)` for cross-module binding contributions

### Navigation 3
Navigation 3 uses typed destinations (`NavKey`), user-owned back stacks (`SnapshotStateList`), and `NavDisplay` instead of `NavHost`.

```kotlin
@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route
    
    @Serializable
    data class Profile(val userId: String) : Route
}

// KMP: use SavedStateConfiguration with SerializersModule
val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Route>()
        }
    }
}
```

**Rules:**
- Routes implement `NavKey` and are `@Serializable`
- Back stack is user-owned: `SnapshotStateList<Route>` managed by `NavigationState`
- `Navigator` class provides `navigate()` and `pop()` methods
- Destinations defined via `entryProvider` lambda (not `NavHost` DSL)
- Use `NavDisplay` composable to render the back stack
- For KMP non-JVM: use `SavedStateConfiguration` with `SerializersModule` + `polymorphic` registration
- ViewModels scoped to entries via `lifecycle-viewmodel-navigation3`
- Use `subclassesOfSealed<Route>()` for sealed type polymorphic serialization in KMP

---

## 7. Build Configuration & Tooling
### Kotlin DSL + Version Catalog (`libs.versions.toml`)
```toml
[versions]
kotlin = "2.1.0"
compose = "1.7.3"
nav3 = "1.1.0"
metro = "0.9.4"
lifecycle = "2.9.0"
kotlinxSerialization = "1.9.0"

[libraries]
compose-ui = { module = "androidx.compose.ui:ui", version.ref = "compose" }
lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle" }
lifecycle-viewmodel-navigation3 = { module = "androidx.lifecycle:lifecycle-viewmodel-navigation3", version.ref = "lifecycle" }
navigation3-runtime = { module = "androidx.navigation3:navigation3-runtime", version.ref = "nav3" }
navigation3-ui = { module = "androidx.navigation3:navigation3-ui", version.ref = "nav3" }
navigation3-browser = { module = "com.github.terrakok:navigation3-browser", version = "0.2.0" }
metro-runtime = { module = "dev.zacsweers.metro:metro-runtime", version.ref = "metro" }
metro-viewmodel = { module = "dev.zacsweers.metro:metrox-viewmodel-compose", version.ref = "metro" }
kotlinx-serialization-core = { module = "org.jetbrains.kotlinx:kotlinx-serialization-core", version.ref = "kotlinxSerialization" }
```

### KMP Targets (`build.gradle.kts`)
```kotlin
kotlin {
    jvm()
    android {}
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.viewmodel.navigation3)
            implementation(libs.navigation3.runtime)
            implementation(libs.navigation3.ui)
            implementation(libs.metro.runtime)
            implementation(libs.metro.viewmodel)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
```

---

## 8. Naming Conventions
| Type | Format | Example | Location |
|------|--------|---------|----------|
| Screen | `[Feature]Screen` | `HomeScreen`, `ProfileScreen` | `ui.{feature}/` |
| ViewModel | `[Feature]ViewModel` | `HomeViewModel` | `ui.{feature}/` |
| Business Logic | `[Feature]Service` | `AuthService`, `UserService` | `domain.{feature}/` |
| Repository (direct impl) | `[Feature]Repository` | `AuthRepository` | `data.{feature}/` |
| Packages | `com.example.app.ui.{feature}` | `com.example.app.ui.home` | Root structure |

---

## 9. Testing Strategy
| Layer | Framework | Scope | Rule |
|-------|-----------|-------|------|
| ViewModel | `turbine`, `mockk` | Flow pipelines, state updates | Mandatory |
| Service/UseCase | `turbine`, `mockk` | Business logic, flow pipelines | Mandatory |
| Repository | `turbine`, `mockk` | Remote/local fallback, caching | Mandatory |
| UI Compose | `createComposeRule`, snapshot tests | Complex layouts, custom components | Optional, critical paths only |
| Integration | `mockk`, real DI | Auth, payments, offline sync | Only when ROI justifies |

**Rules:**
- Prefer unit tests over UI tests
- Test state flows with `turbine` assertions (`awaitItem()`, `awaitComplete()`, `expectError()`)
- Mock external dependencies, never mock ViewModels in UI tests
- Keep Compose tests focused on rendering correctness and interaction handling

---

## 10. Quick Start & Feature Workflow
1. Create feature package: `com.example.app.ui.{feature}`
2. Add `*Screen.kt`, `*ViewModel.kt`, `*Components.kt`
3. Define `*Service` in `domain.{feature}`
4. Create repository class (direct impl) in `data.{feature}`, inject via `@Inject`
5. Register DI with `@DependencyGraph` + `@Provides` (Metro)
6. Define `@Serializable` route implementing `NavKey` and add to `entryProvider`
7. Write unit tests for ViewModel & Service (if requested)
8. Inject ViewModel via `metroViewModel<T>()` in Compose
9. Run on the desktop target before iOS and Android
