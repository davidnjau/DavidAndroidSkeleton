# Android Setup

A brief description of your project goes here.

## Table of Contents

- [Project Name](#project-name)
  - [Table of Contents](#table-of-contents)
  - [Binding](#binding)
  - [FHIR Implementation](#fhir-implementation)


## Description

Provide a concise overview of your project. Explain what it does, why it's useful, and any other relevant context.

## Binding

The following will enable view binding in android.

- Add following in the build.gradle.kts
```
buildFeatures{
    viewBinding = true
}
```
- Install the following dependecies
```
val navVersion = "2.7.4"

// Java language implementation
implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

// Kotlin
implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

// Feature module Support
implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

// Testing Navigation
androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

// Jetpack Compose Integration
implementation("androidx.navigation:navigation-compose:$navVersion")

```
- Create a navigation folder and add nav_graph.xml
```
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.dave.davidtest.HomeFragment"
        android:label="fragment_home"
        tools:layout="@layout/fragment_home" >
        <action
            android:id="@+id/action_homeFragment_to_profileFragment"
            app:destination="@id/profileFragment" />
    </fragment>

    <fragment
        android:id="@+id/profileFragment"
        android:name="com.dave.davidtest.ProfileFragment"
        android:label="fragment_profile"
        tools:layout="@layout/fragment_profile" />

</navigation>
```

- Initial fragment setup
```
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var fragmentHomeBinding : FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        fragmentHomeBinding = binding
        binding.tvHome.text = "Test Data"
        binding.btnNext.setOnClickListener {
            view.findNavController().navigate(R.id.profileFragment)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        fragmentHomeBinding = null
        super.onDestroyView()
    }

}
```

- Main Activity
```
<androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"

        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph" />
```

- Pass data to new fragment
```

- Fragment 1

binding.btnNext.setOnClickListener {
    val bundle = bundleOf("id" to "1")
    view.findNavController().navigate(R.id.profileFragment,bundle)
}

- Fragment 2

binding.tvUid.text = arguments?.getString("id")
    
```


## FHIR Implementation

Use the following to install FHIR

- Sample build.gradle.kts
```
plugins {
  id("com.android.application")
  id("kotlin-android")
  id("androidx.navigation.safeargs.kotlin")
}

android {
  namespace = "com.dave.zanzibar"
  compileSdk = 33
  defaultConfig {
    applicationId = "com.dave.zanzibar"
    minSdk = 24
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    manifestPlaceholders["appAuthRedirectScheme"] = applicationId!!
    buildFeatures.buildConfig = true
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  buildFeatures { viewBinding = true }
  compileOptions {
    // Flag to enable support for the new language APIs
    // See https://developer.android.com/studio/write/java8-support
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlin { jvmToolchain(11) }
  packaging {
    resources.excludes.addAll(
      listOf(
        "META-INF/ASL-2.0.txt",
        "META-INF/LGPL-3.0.txt",
        "META-INF/LICENSE.md",
        "META-INF/NOTICE.md",
        "META-INF/sun-jaxb.episode",
        "META-INF/DEPENDENCIES"
      )
    )
  }
}

configurations.all {
  resolutionStrategy {
    force(HapiFhir.caffeine)

    force(HapiFhir.fhirBase)
    force(HapiFhir.fhirClient)
    force(HapiFhir.fhirCoreConvertors)

    force(HapiFhir.fhirCoreDstu2)
    force(HapiFhir.fhirCoreDstu2016)
    force(HapiFhir.fhirCoreDstu3)
    force(HapiFhir.fhirCoreR4)
    force(HapiFhir.fhirCoreR4b)
    force(HapiFhir.fhirCoreR5)
    force(HapiFhir.fhirCoreUtils)

    force(HapiFhir.structuresDstu2)
    force(HapiFhir.structuresDstu3)
    force(HapiFhir.structuresR4)
    force(HapiFhir.structuresR5)

    force(HapiFhir.validation)
    force(HapiFhir.validationDstu3)
    force(HapiFhir.validationR4)
    force(HapiFhir.validationR5)
  }
}

object Versions {
  const val caffeine = "2.9.1"

  // Hapi FHIR and HL7 Core Components are interlinked.
  // Newer versions of HapiFhir don't work on Android due to the use of Caffeine 3+
  // Wait for this to release (6.3): https://github.com/hapifhir/hapi-fhir/pull/4196
  const val hapiFhir = "6.0.1"

  // Newer versions don't work on Android due to Apache Commons Codec:
  // Wait for this fix: https://github.com/hapifhir/org.hl7.fhir.core/issues/1046
  const val hapiFhirCore = "5.6.36"
}

object HapiFhir {
  const val fhirBase = "ca.uhn.hapi.fhir:hapi-fhir-base:${Versions.hapiFhir}"
  const val fhirClient = "ca.uhn.hapi.fhir:hapi-fhir-client:${Versions.hapiFhir}"
  const val structuresDstu2 = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2:${Versions.hapiFhir}"
  const val structuresDstu3 = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu3:${Versions.hapiFhir}"
  const val structuresR4 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4:${Versions.hapiFhir}"
  const val structuresR4b = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4b:${Versions.hapiFhir}"
  const val structuresR5 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r5:${Versions.hapiFhir}"

  const val validation = "ca.uhn.hapi.fhir:hapi-fhir-validation:${Versions.hapiFhir}"
  const val validationDstu3 =
    "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-dstu3:${Versions.hapiFhir}"
  const val validationR4 = "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:${Versions.hapiFhir}"
  const val validationR5 = "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r5:${Versions.hapiFhir}"

  const val fhirCoreDstu2 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2:${Versions.hapiFhirCore}"
  const val fhirCoreDstu2016 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2016may:${Versions.hapiFhirCore}"
  const val fhirCoreDstu3 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu3:${Versions.hapiFhirCore}"
  const val fhirCoreR4 = "ca.uhn.hapi.fhir:org.hl7.fhir.r4:${Versions.hapiFhirCore}"
  const val fhirCoreR4b = "ca.uhn.hapi.fhir:org.hl7.fhir.r4b:${Versions.hapiFhirCore}"
  const val fhirCoreR5 = "ca.uhn.hapi.fhir:org.hl7.fhir.r5:${Versions.hapiFhirCore}"
  const val fhirCoreUtils = "ca.uhn.hapi.fhir:org.hl7.fhir.utilities:${Versions.hapiFhirCore}"
  const val fhirCoreConvertors = "ca.uhn.hapi.fhir:org.hl7.fhir.convertors:${Versions.hapiFhirCore}"

  // Runtime dependency that is required to run FhirPath (also requires minSDK of 26).
  // Version 3.0 uses java.lang.System.Logger, which is not available on Android
  // Replace for Guava when this PR gets merged: https://github.com/hapifhir/hapi-fhir/pull/3977
  const val caffeine = "com.github.ben-manes.caffeine:caffeine:${Versions.caffeine}"
}

dependencies {
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
  implementation("androidx.activity:activity-ktx:1.7.2")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("androidx.fragment:fragment-ktx:1.6.0")
  implementation("androidx.recyclerview:recyclerview:1.3.0")
  implementation("androidx.work:work-runtime-ktx:2.8.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.22")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
  implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
  implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  implementation("com.google.android.material:material:1.9.0")
  implementation("com.jakewharton.timber:timber:5.0.1")
  implementation("net.openid:appauth:0.11.1")
  implementation("com.auth0.android:jwtdecode:2.0.1")
  implementation("com.google.android.fhir:engine:0.1.0-beta03")
  implementation("com.google.android.fhir:data-capture:1.0.0")
  implementation("com.google.android.fhir:knowledge:0.1.0-alpha01")
  implementation("com.google.android.fhir:workflow:0.1.0-alpha03")
}

```

- Add the following classes:

FhirSyncWorker.class, TimestampBasedDownloadWorkManagerImpl.class, ValueSetResolver.class, DemoDataStore.class, FhirApplication.class, ReferenceUrlResolver.class

- FhirSyncWorker.class
```
class FhirSyncWorker(appContext: Context, workerParams: WorkerParameters) :
    FhirSyncWorker(appContext, workerParams) {

    override fun getDownloadWorkManager(): DownloadWorkManager {
        return TimestampBasedDownloadWorkManagerImpl(
            FhirApplication.dataStore(applicationContext))
    }

    override fun getConflictResolver() = AcceptLocalConflictResolver

    override fun getFhirEngine() = FhirApplication.fhirEngine(applicationContext)
}
```

- TimestampBasedDownloadWorkManagerImpl.class
```

class TimestampBasedDownloadWorkManagerImpl(private val dataStore: DemoDataStore) :
    DownloadWorkManager {
    private val resourceTypeList = ResourceType.values().map { it.name }
    private val urls = LinkedList(
        listOf("Patient?address-city=NAIROBI&_sort=_lastUpdated", "Practitioner","RelatedPerson")
    )

    override suspend fun getNextRequest(): Request? {
        var url = urls.poll() ?: return null

        val resourceTypeToDownload =
            ResourceType.fromCode(url.findAnyOf(resourceTypeList, ignoreCase = true)!!.second)
        dataStore.getLastUpdateTimestamp(resourceTypeToDownload)?.let {
            url = affixLastUpdatedTimestamp(url, it)
        }
        return Request.of(url)
    }

    override suspend fun getSummaryRequestUrls(): Map<ResourceType, String> {
        return urls.associate { url ->
            val resourceType = ResourceType.fromCode(url.substringBefore("?"))
            val summaryParam = if (resourceType != ResourceType.Practitioner) {
                "&${SyncDataParams.SUMMARY_KEY}=${SyncDataParams.SUMMARY_COUNT_VALUE}"
            } else {
                ""
            }

            resourceType to url.plus(summaryParam)
        }
    }


    override suspend fun processResponse(response: Resource): Collection<Resource> {
        // As per FHIR documentation :
        // If the search fails (cannot be executed, not that there are no matches), the
        // return value SHALL be a status code 4xx or 5xx with an OperationOutcome.
        // See https://www.hl7.org/fhir/http.html#search for more details.
        if (response is OperationOutcome) {
            throw FHIRException(response.issueFirstRep.diagnostics)
        }

        // If the resource returned is a List containing Patients, extract Patient references and fetch
        // all resources related to the patient using the $everything operation.
        if (response is ListResource) {
            for (entry in response.entry) {
                val reference = Reference(entry.item.reference)
                if (reference.referenceElement.resourceType.equals("Patient")) {
                    val patientUrl = "${entry.item.reference}/\$everything"
                    urls.add(patientUrl)
                }
            }
        }

        // If the resource returned is a Bundle, check to see if there is a "next" relation referenced
        // in the Bundle.link component, if so, append the URL referenced to list of URLs to download.
        if (response is Bundle) {
            val nextUrl =
                response.link.firstOrNull { component -> component.relation == "next" }?.url
            if (nextUrl != null) {
                urls.add(nextUrl)
            }
        }

        // Finally, extract the downloaded resources from the bundle.
        var bundleCollection: Collection<Resource> = mutableListOf()
        if (response is Bundle && response.type == Bundle.BundleType.SEARCHSET) {
            bundleCollection =
                response.entry
                    .map { it.resource }
                    .also { extractAndSaveLastUpdateTimestampToFetchFutureUpdates(it) }
        }
        return bundleCollection
    }

    private suspend fun extractAndSaveLastUpdateTimestampToFetchFutureUpdates(
        resources: List<Resource>,
    ) {
        resources
            .groupBy { it.resourceType }
            .entries
            .map { map ->
                dataStore.saveLastUpdatedTimestamp(
                    map.key,
                    map.value.maxOfOrNull { it.meta.lastUpdated }?.toTimeZoneString() ?: "",
                )
            }
    }
}

/**
 * Affixes the last updated timestamp to the request URL.
 *
 * If the request URL includes the `$everything` parameter, the last updated timestamp will be
 * attached using the `_since` parameter. Otherwise, the last updated timestamp will be attached
 * using the `_lastUpdated` parameter.
 */
private fun affixLastUpdatedTimestamp(url: String, lastUpdated: String): String {
    var downloadUrl = url

    // Affix lastUpdate to a $everything query using _since as per:
    // https://hl7.org/fhir/operation-patient-everything.html
    if (downloadUrl.contains("\$everything")) {
        downloadUrl = "$downloadUrl?_since=$lastUpdated"
    }

    // Affix lastUpdate to non-$everything queries as per:
    // https://hl7.org/fhir/operation-patient-everything.html
    if (!downloadUrl.contains("\$everything") || !downloadUrl.contains("Practitioner")) {
        downloadUrl = "$downloadUrl&_lastUpdated=gt$lastUpdated"
    }

    // Do not modify any URL set by a server that specifies the token of the page to return.
    if (downloadUrl.contains("&page_token")) {
        downloadUrl = url
    }

    return downloadUrl
}

private fun Date.toTimeZoneString(): String {
    val simpleDateFormat =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    return simpleDateFormat.format(this.toInstant())
}
```

- ValueSetResolver.class
```
abstract class ValueSetResolver : ExternalAnswerValueSetResolver {

    companion object {
        private lateinit var fhirEngine: FhirEngine

        fun init(context: Context) {
            fhirEngine = FhirApplication.fhirEngine(context)
        }

        private suspend fun fetchValueSetFromDb(uri: String): List<Coding> {

            val valueSets = fhirEngine.search<ValueSet> { filter(ValueSet.URL, { value = uri }) }

            if (valueSets.isEmpty())
                return listOf(Coding().apply { display = "No referral facility available." })
            else {
                val valueSetList = ArrayList<Coding>()
                for (valueSet in valueSets) {
                    for (item in valueSet.expansion.contains) {
                        valueSetList.add(
                            Coding().apply {
                                system = item.system
                                code = item.code
                                display = item.display
                            }
                        )
                    }
                }
                return valueSetList
            }
        }
    }

    override suspend fun resolve(uri: String): List<Coding> {
        return fetchValueSetFromDb(uri)
    }
}
```

- DemoDataStore.class
```
private val Context.dataStorage: DataStore<Preferences> by
preferencesDataStore(name = "demo_app_storage")

class DemoDataStore(private val context: Context) {

  suspend fun saveLastUpdatedTimestamp(resourceType: ResourceType, timestamp: String) {
    context.dataStorage.edit { pref -> pref[stringPreferencesKey(resourceType.name)] = timestamp }
  }

  suspend fun getLastUpdateTimestamp(resourceType: ResourceType): String? {
    return context.dataStorage.data.first()[stringPreferencesKey(resourceType.name)]
  }
}
```

- FhirApplication.class

For the FhirSyncWorker, import the local fhir. 

import com.dave.zanzibar.fhir.data.FhirSyncWorker //Import the local fhir

```
class FhirApplication : Application(), DataCaptureConfig.Provider {
    // Only initiate the FhirEngine when used for the first time, not when the app is created.
    private val fhirEngine: FhirEngine by lazy { constructFhirEngine() }

    private var dataCaptureConfig: DataCaptureConfig? = null

    private val dataStore by lazy { DemoDataStore(this) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        FhirEngineProvider.init(
            FhirEngineConfiguration(
                enableEncryptionIfSupported = false,
                DatabaseErrorStrategy.RECREATE_AT_OPEN,
                ServerConfiguration(
                    "http://chanjoke.intellisoftkenya.com:8900/fhir/",
                    httpLogger =
                    HttpLogger(
                        HttpLogger.Configuration(
                            if (BuildConfig.DEBUG) HttpLogger.Level.BODY else HttpLogger.Level.BASIC
                        )
                    ) { Timber.tag("App-HttpLog").d(it) }
                )
            )
        )
        Sync.oneTimeSync<FhirSyncWorker>(this)

        dataCaptureConfig =
            DataCaptureConfig().apply {
                urlResolver = ReferenceUrlResolver(this@FhirApplication as Context)
                valueSetResolverExternal = object : ValueSetResolver() {}
                xFhirQueryResolver = XFhirQueryResolver { fhirEngine.search(it) }

            }
    }

    private fun constructFhirEngine(): FhirEngine {
        return FhirEngineProvider.getInstance(this)
    }

    companion object {
        fun fhirEngine(context: Context) = (context.applicationContext as FhirApplication).fhirEngine

        fun dataStore(context: Context) = (context.applicationContext as FhirApplication).dataStore
    }

    override fun getDataCaptureConfig(): DataCaptureConfig = dataCaptureConfig ?: DataCaptureConfig()
}
```

- ReferenceUrlResolver.class
```
class ReferenceUrlResolver(val context: Context) : UrlResolver {

  override suspend fun resolveBitmapUrl(url: String): Bitmap? {
    val logicalId = getLogicalIdFromFhirUrl(url, ResourceType.Binary)
    val binary = FhirApplication.fhirEngine(context).get<Binary>(logicalId)
    return BitmapFactory.decodeByteArray(binary.data, 0, binary.data.size)
  }
}

/**
 * Returns the logical id of a FHIR Resource URL e.g
 * 1. "https://hapi.fhir.org/baseR4/Binary/1234" returns "1234".
 * 2. "https://hapi.fhir.org/baseR4/Binary/1234/_history/2" returns "1234".
 */
private fun getLogicalIdFromFhirUrl(url: String, resourceType: ResourceType): String {
  return url.substringAfter("${resourceType.name}/").substringBefore("/")
}
```


