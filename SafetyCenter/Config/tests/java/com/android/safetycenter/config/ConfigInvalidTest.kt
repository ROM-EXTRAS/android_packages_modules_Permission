/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.safetycenter.config

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.android.safetycenter.tests.config.safetycenterconfig.R
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runners.Parameterized
import org.junit.runner.RunWith

@RunWith(Parameterized::class)
class ConfigInvalidTest {
    private val context: Context = getApplicationContext()

    data class Params(
        private val testName: String,
        val configResourceId: Int,
        val errorMessage: String,
        val causeErrorMessage: String?
    ) {
        override fun toString() = testName
    }

    @Parameterized.Parameter
    lateinit var params: Params

    @Test
    fun invalidConfig_throws() {
        val inputStream = context.resources.openRawResource(params.configResourceId)
        val thrown = assertThrows(Parser.ParseException::class.java) {
            Parser.parse(inputStream, context.packageName, context.resources)
        }
        assertThat(thrown).hasMessageThat().isEqualTo(params.errorMessage)
        if (params.causeErrorMessage != null) {
            assertThat(thrown.cause).hasMessageThat().isEqualTo(params.causeErrorMessage)
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun parameters() = arrayOf(
            Params(
                "ConfigDynamicSafetySourceDuplicateKey",
                R.raw.config_dynamic_safety_source_duplicate_key,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources"
            ),
            Params(
                "ConfigDynamicSafetySourceInvalidProfile",
                R.raw.config_dynamic_safety_source_invalid_profile,
                "Element safety-source invalid",
                "Attribute profile invalid"
            ),
            Params(
                "ConfigDynamicSafetySourceNoId",
                R.raw.config_dynamic_safety_source_no_id,
                "Element safety-source invalid",
                "Required attribute id missing"
            ),
            Params(
                "ConfigDynamicSafetySourceNoIntent",
                R.raw.config_dynamic_safety_source_no_intent,
                "Element safety-source invalid",
                "Required attribute intentAction missing"
            ),
            Params(
                "ConfigDynamicSafetySourceNoPackage",
                R.raw.config_dynamic_safety_source_no_package,
                "Element safety-source invalid",
                "Required attribute packageName missing"
            ),
            Params(
                "ConfigDynamicSafetySourceNoProfile",
                R.raw.config_dynamic_safety_source_no_profile,
                "Element safety-source invalid",
                "Required attribute profile missing"
            ),
            Params(
                "ConfigDynamicSafetySourceNoSummary",
                R.raw.config_dynamic_safety_source_no_summary,
                "Element safety-source invalid",
                "Required attribute summary missing"
            ),
            Params(
                "ConfigDynamicSafetySourceNoTitle",
                R.raw.config_dynamic_safety_source_no_title,
                "Element safety-source invalid",
                "Required attribute title missing"
            ),
            Params(
                "ConfigDynamicSafetySourceWithPrimaryAndWork",
                R.raw.config_dynamic_safety_source_with_primary_and_work,
                "Element safety-source invalid",
                "Prohibited attribute titleForWork present"
            ),
            Params(
                "ConfigFileCorrupted",
                R.raw.config_file_corrupted,
                "Exception while reading XML",
                null
            ),
            Params(
                "ConfigInternalSafetySourceDuplicateKey",
                R.raw.config_internal_safety_source_duplicate_key,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources"
            ),
            Params(
                "ConfigInternalSafetySourceNoId",
                R.raw.config_internal_safety_source_no_id,
                "Element safety-source invalid",
                "Required attribute id missing"
            ),
            Params(
                "ConfigInternalSafetySourceWithBroadcast",
                R.raw.config_internal_safety_source_with_broadcast,
                "Element safety-source invalid",
                "Prohibited attribute broadcastReceiverClassName present"
            ),
            Params(
                "ConfigInternalSafetySourceWithIntent",
                R.raw.config_internal_safety_source_with_intent,
                "Element safety-source invalid",
                "Prohibited attribute intentAction present"
            ),
            Params(
                "ConfigInternalSafetySourceWithLogging",
                R.raw.config_internal_safety_source_with_logging,
                "Element safety-source invalid",
                "Prohibited attribute disallowLogging present"
            ),
            Params(
                "ConfigInternalSafetySourceWithPackage",
                R.raw.config_internal_safety_source_with_package,
                "Element safety-source invalid",
                "Prohibited attribute packageName present"
            ),
            Params(
                "ConfigInternalSafetySourceWithProfile",
                R.raw.config_internal_safety_source_with_profile,
                "Element safety-source invalid",
                "Prohibited attribute profile present"
            ),
            Params(
                "ConfigInternalSafetySourceWithRefresh",
                R.raw.config_internal_safety_source_with_refresh,
                "Element safety-source invalid",
                "Prohibited attribute allowRefreshOnPageOpen present"
            ),
            Params(
                "ConfigInternalSafetySourceWithSearch",
                R.raw.config_internal_safety_source_with_search,
                "Element safety-source invalid",
                "Prohibited attribute searchTerms present"
            ),
            Params(
                "ConfigInternalSafetySourceWithSeverity",
                R.raw.config_internal_safety_source_with_severity,
                "Element safety-source invalid",
                "Prohibited attribute maxSeverityLevel present"
            ),
            Params(
                "ConfigInternalSafetySourceWithSummary",
                R.raw.config_internal_safety_source_with_summary,
                "Element safety-source invalid",
                "Prohibited attribute summary present"
            ),
            Params(
                "ConfigInternalSafetySourceWithTitle",
                R.raw.config_internal_safety_source_with_title,
                "Element safety-source invalid",
                "Prohibited attribute title present"
            ),
            Params(
                "ConfigInternalSafetySourceWithWork",
                R.raw.config_internal_safety_source_with_work,
                "Element safety-source invalid",
                "Prohibited attribute titleForWork present"
            ),
            Params(
                "ConfigMixedSafetySourceDuplicateKey",
                R.raw.config_mixed_safety_source_duplicate_key,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources"
            ),
            Params(
                "ConfigMixedSafetySourceInvalidType",
                R.raw.config_mixed_safety_source_invalid_type,
                "Element safety-source invalid",
                "Attribute type invalid"
            ),
            Params(
                "ConfigMixedSafetySourceNoType",
                R.raw.config_mixed_safety_source_no_type,
                "Element safety-source invalid",
                "Required attribute type missing"
            ),
            Params(
                "ConfigMixedSafetySourcesGroupDuplicateId",
                R.raw.config_mixed_safety_sources_group_duplicate_id,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources groups"
            ),
            Params(
                "ConfigReferenceInvalid",
                R.raw.config_reference_invalid,
                "String title in safety-sources-group.title is not a reference",
                null
            ),
            Params(
                "ConfigReferenceMissing",
                R.raw.config_reference_missing,
                "Reference @string/reference2 in safety-sources-group.summary missing",
                null
            ),
            Params(
                "ConfigSafetyCenterConfigMissing",
                R.raw.config_safety_center_config_missing,
                "Element safety-center-config missing",
                null
            ),
            Params(
                "ConfigSafetySourcesConfigEmpty",
                R.raw.config_safety_sources_config_empty,
                "Element safety-sources-config invalid",
                "No safety sources groups present"
            ),
            Params(
                "ConfigSafetySourcesConfigMissing",
                R.raw.config_safety_sources_config_missing,
                "Element safety-sources-config missing",
                null
            ),
            Params(
                "ConfigSafetySourcesGroupDuplicateId",
                R.raw.config_safety_sources_group_duplicate_id,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources groups"
            ),
            Params(
                "ConfigSafetySourcesGroupEmpty",
                R.raw.config_safety_sources_group_empty,
                "Element safety-sources-group invalid",
                "Safety sources group empty"
            ),
            Params(
                "ConfigSafetySourcesGroupInvalidIcon",
                R.raw.config_safety_sources_group_invalid_icon,
                "Element safety-sources-group invalid",
                "Attribute statelessIconType invalid"
            ),
            Params(
                "ConfigSafetySourcesGroupNoId",
                R.raw.config_safety_sources_group_no_id,
                "Element safety-sources-group invalid",
                "Required attribute id missing"
            ),
            Params(
                "ConfigSafetySourcesGroupNoSummary",
                R.raw.config_safety_sources_group_no_summary,
                "Element safety-sources-group invalid",
                "Required attribute summary missing"
            ),
            Params(
                "ConfigSafetySourcesGroupNoTitle",
                R.raw.config_safety_sources_group_no_title,
                "Element safety-sources-group invalid",
                "Required attribute title missing"
            ),
            Params(
                "ConfigStaticSafetySourceDuplicateKey",
                R.raw.config_static_safety_source_duplicate_key,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources"
            ),
            Params(
                "ConfigStaticSafetySourceInvalidProfile",
                R.raw.config_static_safety_source_invalid_profile,
                "Element safety-source invalid",
                "Attribute profile invalid"
            ),
            Params(
                "ConfigStaticSafetySourceNoId",
                R.raw.config_static_safety_source_no_id,
                "Element safety-source invalid",
                "Required attribute id missing"
            ),
            Params(
                "ConfigStaticSafetySourceNoIntent",
                R.raw.config_static_safety_source_no_intent,
                "Element safety-source invalid",
                "Required attribute intentAction missing"
            ),
            Params(
                "ConfigStaticSafetySourceNoProfile",
                R.raw.config_static_safety_source_no_profile,
                "Element safety-source invalid",
                "Required attribute profile missing"
            ),
            Params(
                "ConfigStaticSafetySourceNoSummary",
                R.raw.config_static_safety_source_no_summary,
                "Element safety-source invalid",
                "Required attribute summary missing"
            ),
            Params(
                "ConfigStaticSafetySourceNoTitle",
                R.raw.config_static_safety_source_no_title,
                "Element safety-source invalid",
                "Required attribute title missing"
            ),
            Params(
                "ConfigStaticSafetySourcesGroupDuplicateId",
                R.raw.config_static_safety_sources_group_duplicate_id,
                "Element safety-sources-config invalid",
                "Duplicate id id among safety sources groups"
            ),
            Params(
                "ConfigStaticSafetySourcesGroupEmpty",
                R.raw.config_static_safety_sources_group_empty,
                "Element static-safety-sources-group invalid",
                "Static safety sources group empty"
            ),
            Params(
                "ConfigStaticSafetySourcesGroupInvalidType",
                R.raw.config_static_safety_sources_group_invalid_type,
                "Element static-safety-sources-group invalid",
                "Invalid safety source type 3 in safety sources group"
            ),
            Params(
                "ConfigStaticSafetySourcesGroupNoId",
                R.raw.config_static_safety_sources_group_no_id,
                "Element static-safety-sources-group invalid",
                "Required attribute id missing"
            ),
            Params(
                "ConfigStaticSafetySourcesGroupNoTitle",
                R.raw.config_static_safety_sources_group_no_title,
                "Element static-safety-sources-group invalid",
                "Required attribute title missing"
            ),
            Params(
                "ConfigStaticSafetySourceWithBroadcast",
                R.raw.config_static_safety_source_with_broadcast,
                "Element safety-source invalid",
                "Prohibited attribute broadcastReceiverClassName present"
            ),
            Params(
                "ConfigStaticSafetySourceWithLogging",
                R.raw.config_static_safety_source_with_logging,
                "Element safety-source invalid",
                "Prohibited attribute disallowLogging present"
            ),
            Params(
                "ConfigStaticSafetySourceWithPackage",
                R.raw.config_static_safety_source_with_package,
                "Element safety-source invalid",
                "Prohibited attribute packageName present"
            ),
            Params(
                "ConfigStaticSafetySourceWithPrimaryAndWork",
                R.raw.config_static_safety_source_with_primary_and_work,
                "Element safety-source invalid",
                "Prohibited attribute titleForWork present"
            ),
            Params(
                "ConfigStaticSafetySourceWithRefresh",
                R.raw.config_static_safety_source_with_refresh,
                "Element safety-source invalid",
                "Prohibited attribute allowRefreshOnPageOpen present"
            ),
            Params(
                "ConfigStaticSafetySourceWithSeverity",
                R.raw.config_static_safety_source_with_severity,
                "Element safety-source invalid",
                "Prohibited attribute maxSeverityLevel present"
            )
        )
    }
}