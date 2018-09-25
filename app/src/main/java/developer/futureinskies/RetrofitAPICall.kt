package developer.futureinskies

import fortuner.ai.Login.DataModel.MissionResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


/**
 * Created by Nishanth on 20/9/18.
 */
interface RetrofitAPICall {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */

    @GET("launches/")
    fun getMission(@Header("X-Requested-With") xrequest: String, @Header("Authorization") basic: String): Call<ArrayList<MissionResponseData>>

}