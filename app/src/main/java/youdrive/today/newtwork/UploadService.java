package youdrive.today.newtwork;

import java.util.Map;

import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.mime.TypedFile;
import rx.Observable;
import youdrive.today.response.UploadCareResponse;
import youdrive.today.response.UploadGroupResponse;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 11/1/15.
 */
public interface UploadService {

    @Multipart
    @POST("/base/")
    Observable<UploadCareResponse>uploadFile(@Part("UPLOADCARE_PUB_KEY") String publicKey,
                                   @Part("UPLOADCARE_STORE") int store,
                                   @Part("file") TypedFile file);





    @Multipart
    @POST("/group/")
    Observable<UploadGroupResponse> uploadGroup(@Part("pub_key" ) String publicKey, @PartMap Map<String ,String> params);

}
