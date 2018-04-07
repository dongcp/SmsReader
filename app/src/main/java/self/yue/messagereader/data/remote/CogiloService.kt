package self.yue.messagereader.data.remote

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by dongc on 10/16/2017.
 */
interface CogiloService {
  @POST("money_sms_log")
  fun sendSMS(@Body data: JsonObject): Call<ResponseBody>
}