/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.retriever.criteria.interfaces;

import eu.esens.espdvcd.builder.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 * @author konstantinos
 */
public interface IECertisService {
    
    @GET(Constants.AVAILABLE_CRITERIA + "{id}/")
    Call<ResponseBody> getCriterionR2(@Path("id") String cID);
    
}
