package self.yue.messagereader.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dongc on 10/17/2017.
 */

public class CogiloServiceBuilder {
  private static final String BASE_URL = "https://cogilo.com/api/";
  private static final int CONNECTION_TIME_OUT = 60000;
  private static final int READ_TIME_OUT = 60000;

  private static Retrofit sRetrofit;

  private static CogiloService sService;

  public static void initRetrofit() {
    if (sRetrofit == null) {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
              .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
              .readTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
              .addInterceptor(interceptor)
              .build();

      sRetrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .client(client)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
  }

  public static CogiloService getService() {
    if (sService == null)
      sService = sRetrofit.create(CogiloService.class);
    return sService;
  }
}
