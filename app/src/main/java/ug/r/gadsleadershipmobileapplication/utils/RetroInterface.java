package ug.r.gadsleadershipmobileapplication.utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ug.r.gadsleadershipmobileapplication.views.LearningLeadersFragment;
import ug.r.gadsleadershipmobileapplication.views.SkillIQLeadersFragment;

public interface RetroInterface {

    @GET("api/hours")
    Call<ArrayList<LearningLeadersFragment.Leader>> learning();

    @GET("api/skilliq")
    Call<ArrayList<SkillIQLeadersFragment.Leader>> iq_leaders();


    @FormUrlEncoded
    @POST("1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse")
    Call<Void> submit_project(
            @Field("entry.1877115667") String first_name,
            @Field("entry.1824927963") String email_address,
            @Field("entry.2006916086") String last_name,
            @Field("entry.284483984") String project_link
    );
}
