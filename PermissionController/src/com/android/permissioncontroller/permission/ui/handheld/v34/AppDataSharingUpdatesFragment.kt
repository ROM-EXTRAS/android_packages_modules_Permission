package com.android.permissioncontroller.permission.ui.handheld.v34

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.UserHandle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import com.android.permissioncontroller.Constants.EXTRA_SESSION_ID
import com.android.permissioncontroller.R
import com.android.permissioncontroller.permission.model.v34.DataSharingUpdateType
import com.android.permissioncontroller.permission.ui.handheld.PermissionsFrameFragment
import com.android.permissioncontroller.permission.ui.handheld.pressBack
import com.android.permissioncontroller.permission.ui.model.v34.AppDataSharingUpdatesViewModel
import com.android.permissioncontroller.permission.ui.model.v34.AppDataSharingUpdatesViewModel.AppLocationDataSharingUpdateUiInfo
import com.android.permissioncontroller.permission.utils.KotlinUtils
import com.android.permissioncontroller.permission.utils.StringUtils
import java.text.Collator

/** Fragment to display data sharing updates for installed apps. */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class AppDataSharingUpdatesFragment : PermissionsFrameFragment() {
    private lateinit var viewModel: AppDataSharingUpdatesViewModel
    private lateinit var collator: Collator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = getString(R.string.data_sharing_updates_title)
        setHasOptionsMenu(true)
        collator = Collator.getInstance(requireContext().resources.configuration.locales[0])

        val ab = activity?.actionBar
        ab?.setDisplayHomeAsUpEnabled(true)

        viewModel = AppDataSharingUpdatesViewModel(requireActivity().application)

        if (preferenceScreen == null) {
            addPreferencesFromResource(R.xml.app_data_sharing_updates)
            setLoading(/* loading= */ true, /* animate= */ false)
        }

        viewModel.appLocationDataSharingUpdateUiInfoLiveData.observe(this, this::updatePreferences)
    }

    override fun setDivider(divider: Drawable?) {
        super.setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.pressBack()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updatePreferences(updateUiInfos: List<AppLocationDataSharingUpdateUiInfo>) {
        setLoading(/* loading= */ false, /* animate= */ true)

        if (updateUiInfos.isNotEmpty()) {
            showUpdatesPresentUi()
        } else {
            showNoUpdatesPresentUi()
        }

        val preferenceKeysToShow =
            updateUiInfos.map {
                createUpdatePreferenceKey(it.packageName, it.userHandle, it.dataSharingUpdateType)
            }

        val updatesCategory =
            preferenceScreen.findPreference<PreferenceCategory>(
                LAST_PERIOD_UPDATES_PREFERENCE_CATEGORY_ID)
                ?: return
        for (i in 0 until (updatesCategory.preferenceCount)) {
            // Remove preferences that no longer need to be shown.
            if (!preferenceKeysToShow.contains(updatesCategory.getPreference(i).key)) {
                updatesCategory.removePreference(updatesCategory.getPreference(i))
            }
        }

        updateUiInfos.forEach { updateUiInfo ->
            val key =
                createUpdatePreferenceKey(
                    updateUiInfo.packageName,
                    updateUiInfo.userHandle,
                    updateUiInfo.dataSharingUpdateType)
            if (updatesCategory.findPreference<AppDataSharingUpdatePreference>(key) != null) {
                // If a preference is already shown, don't recreate it.
                return@forEach
            }
            val appDataSharingUpdatePreference =
                AppDataSharingUpdatePreference(
                    requireActivity().application,
                    updateUiInfo.packageName,
                    updateUiInfo.userHandle,
                    requireActivity().applicationContext)
            appDataSharingUpdatePreference.apply {
                this.key = key
                title =
                    KotlinUtils.getPackageLabel(
                        requireActivity().application,
                        updateUiInfo.packageName,
                        updateUiInfo.userHandle)
                summary = getSummaryForLocationUpdateType(updateUiInfo.dataSharingUpdateType)
                preferenceClick =
                    View.OnClickListener { _ ->
                        viewModel.startAppLocationPermissionPage(
                            requireActivity(), updateUiInfo.packageName, updateUiInfo.userHandle)
                    }
                updatesCategory.addPreference(this)
            }
        }
        KotlinUtils.sortPreferenceGroup(updatesCategory, this::compareUpdatePreferences, false)
    }

    private fun getSummaryForLocationUpdateType(type: DataSharingUpdateType): String {
        return when (type) {
            DataSharingUpdateType.ADDS_ADVERTISING_PURPOSE ->
                getString(R.string.shares_location_with_third_parties_for_advertising)
            DataSharingUpdateType.ADDS_SHARING_WITHOUT_ADVERTISING_PURPOSE ->
                getString(R.string.shares_location_with_third_parties)
            DataSharingUpdateType.ADDS_SHARING_WITH_ADVERTISING_PURPOSE ->
                getString(R.string.shares_location_with_third_parties_for_advertising)
            else -> throw IllegalArgumentException("Invalid DataSharingUpdateType: $type")
        }
    }

    private fun showUpdatesPresentUi() {
        if (preferenceScreen == null) {
            return
        }
        val detailsPreference =
            preferenceScreen?.findPreference<AppDataSharingDetailsPreference>(DETAILS_PREFERENCE_ID)
        val footerPreference =
            preferenceScreen?.findPreference<AppDataSharingUpdatesFooterPreference>(
                FOOTER_PREFERENCE_ID)
        val dataSharingUpdatesCategory =
            preferenceScreen?.findPreference<PreferenceCategory>(
                LAST_PERIOD_UPDATES_PREFERENCE_CATEGORY_ID)

        detailsPreference?.let {
            it.showNoUpdates = false
            it.isVisible = true
        }
        dataSharingUpdatesCategory?.let {
            it.title =
                StringUtils.getIcuPluralsString(requireContext(), R.string.updated_in_last_days, 30)
            it.isVisible = true
        }
        footerPreference?.let {
            it.footerMessage = getString(R.string.data_sharing_updates_footer_message)
            it.footerLink = getString(R.string.learn_about_data_sharing)
            it.onFooterLinkClick =
                View.OnClickListener { viewModel.openSafetyLabelsHelpCenterPage(requireActivity()) }
            it.isVisible = true
        }
    }

    // TODO(b/261666772): Once spec is final, consider extracting common elements with
    //  showUpdatesPresentUi into a separate method.
    private fun showNoUpdatesPresentUi() {
        if (preferenceScreen == null) {
            return
        }
        val detailsPreference =
            preferenceScreen?.findPreference<AppDataSharingDetailsPreference>(DETAILS_PREFERENCE_ID)
        val footerPreference =
            preferenceScreen?.findPreference<AppDataSharingUpdatesFooterPreference>(
                FOOTER_PREFERENCE_ID)
        val dataSharingUpdatesCategory =
            preferenceScreen?.findPreference<PreferenceCategory>(
                LAST_PERIOD_UPDATES_PREFERENCE_CATEGORY_ID)

        detailsPreference?.let {
            it.showNoUpdates = true
            it.isVisible = true
        }
        dataSharingUpdatesCategory?.let { it.isVisible = false }
        footerPreference?.let {
            it.footerMessage = getString(R.string.data_sharing_updates_footer_message)
            it.footerLink = getString(R.string.learn_about_data_sharing)
            it.onFooterLinkClick =
                View.OnClickListener { viewModel.openSafetyLabelsHelpCenterPage(requireActivity()) }
            it.isVisible = true
        }
    }

    /** Creates an identifier for a preference. */
    private fun createUpdatePreferenceKey(
        packageName: String,
        user: UserHandle,
        update: DataSharingUpdateType
    ): String {
        return "$packageName:${user.identifier}:${update.name}"
    }

    private fun compareUpdatePreferences(lhs: Preference, rhs: Preference): Int {
        var result = collator.compare(lhs.title.toString(), rhs.title.toString())
        if (result == 0) {
            result = lhs.key.compareTo(rhs.key)
        }
        return result
    }

    /** Companion object for [AppDataSharingUpdatesFragment]. */
    companion object {
        /**
         * Creates a [Bundle] with the arguments needed by this fragment.
         *
         * @param sessionId the current session ID
         * @return a [Bundle] with all of the required args
         */
        fun createArgs(sessionId: Long) = Bundle().apply { putLong(EXTRA_SESSION_ID, sessionId) }

        private const val DETAILS_PREFERENCE_ID = "details"
        private const val FOOTER_PREFERENCE_ID = "info_footer"
        private const val LAST_PERIOD_UPDATES_PREFERENCE_CATEGORY_ID = "last_period_updates"
    }
}