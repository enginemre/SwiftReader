package com.engin.swiftreader

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.engin.swiftreader.common.util.KeyConstants.GG_SIGNUP
import com.engin.swiftreader.common.util.collectWithLifecycle
import com.engin.swiftreader.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var gso: GoogleSignInOptions

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var googleSignInClient: GoogleSignInClient

    private val authenticationViewModel by viewModels<AuthenticationViewModel>()

    private val navDestinationChangeListener =
        NavController.OnDestinationChangedListener { navController, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> showFullScreen()
                R.id.loginFragment -> showFullScreen()
                R.id.signUpFragment -> showFullScreen()
                R.id.readDetailFragment -> showFullScreen()
                R.id.playFragment -> hideBottomBar()
                R.id.cameraFlow -> showFullScreen()
                else -> showComponents()
            }
        }

    private val registerActivityLauncherGoogleSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                task.addOnCompleteListener {
                    if (task.isSuccessful) {
                        if (result.data?.getBooleanExtra(GG_SIGNUP, false) == true) {
                            authenticationViewModel.signUpUser(
                                email = task.result.email,
                                name = task.result.displayName
                            )
                        } else {
                            authenticationViewModel.loginUser(
                                email = task.result.email,
                                value = true
                            )
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            R.string.error_google_login,
                            Snackbar.LENGTH_LONG
                        ).show()
                        authenticationViewModel.signOut()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initializeNavigation()
        arrangeLastPosition(savedInstanceState)
        observeData()
    }

    private fun observeData() {
        collectWithLifecycle(authenticationViewModel.signInWithGoogle) {
            handleGoogleAuth(it)
        }
        collectWithLifecycle(authenticationViewModel.signUpWithGoogle) {
            signUpWithGoogle()
        }
    }

    private fun handleGoogleAuth(isLoginAction: Boolean) {
        if (isLoginAction) {
            if (!checkGoogleSignIn().isNullOrEmpty()) {
                authenticationViewModel.loginUser(value = true)
            } else {
                signInWithGoogle()
            }
        } else {
            signOutWithGoogle()
        }
    }

    private fun signOutWithGoogle() {
        googleSignInClient.signOut()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBundle("nav_state", navController.saveState())
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun checkGoogleSignIn(): String? {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        return account?.email
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        registerActivityLauncherGoogleSignIn.launch(signInIntent)
    }

    private fun signUpWithGoogle() {
        val signUpIntent: Intent = googleSignInClient.signInIntent
        signUpIntent.putExtra(GG_SIGNUP, true)
        registerActivityLauncherGoogleSignIn.launch(signUpIntent)
    }

    private fun arrangeLastPosition(savedInstanceState: Bundle?) {
        if (savedInstanceState?.getBundle("nav_state") != null) {
            navController.restoreState(savedInstanceState.getBundle("nav_state"))
        }
    }

    private fun initializeNavigation() {
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.homeFlow, R.id.cameraFlow, R.id.profileFlow))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomFabView.setOnClickListener { navController.navigate(NavGraphDirections.actionGlobalCameraFlow()) }
        navController.addOnDestinationChangedListener(navDestinationChangeListener)
        navController.setGraph(R.navigation.base_graph)
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun showFullScreen() {
        hideAppBar()
        hideBottomBar()
    }

    private fun showComponents() {
        showAppBar()
        showBottomBar()
    }

    private fun showAppBar() {
        binding.appbar.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideAppBar() {
        binding.appbar.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
    }

    private fun hideBottomBar() {
        binding.bottomAppBar.visibility = View.GONE
        binding.bottomFabView.visibility = View.GONE
    }

    private fun showBottomBar() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.bottomFabView.visibility = View.VISIBLE
    }

}