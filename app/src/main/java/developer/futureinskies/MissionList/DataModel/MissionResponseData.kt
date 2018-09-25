package fortuner.ai.Login.DataModel

class MissionResponseData {

    var flight_number: String? = null
    var mission_name: String? = null
    var launch_year: String? = null
    var launch_date_unix: Long? = null
    var launch_success: Boolean? = null

    var launch_site: LaunchSiteData? = null

    inner class LaunchSiteData {
        var site_name: String? = null
        var site_name_long: String? = null
    }

    var links: LinksData? = null

    inner class LinksData {
        var mission_patch_small: String? = null
        var wikipedia: String? = null
        var video_link: String? = null
    }

    var rocket: RocketsData? = null

    inner class RocketsData {
        var rocket_name: String? = null
        var rocket_type: String? = null

        var second_stage: SecondStage? = null

        inner class SecondStage {

            lateinit var payloads: ArrayList<PayLoadsData>

            inner class PayLoadsData {
                var payload_type: String? = null
                var nationality: String? = null
                var payload_id: String? = null
                var orbit: String? = null
            }
        }
    }
}