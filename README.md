# Android Setup

A brief description of your project goes here.

## Table of Contents

- [Project Name](#project-name)
  - [Table of Contents](#table-of-contents)
  - [Binding](#binding)


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


## Installation

Explain how to install and set up your project. Include any prerequisites or dependencies. You can use code blocks to show commands:



