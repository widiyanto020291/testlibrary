package com.transmedika.transmedikakitui.models.bean.join;

import com.transmedika.transmedikakitui.models.bean.json.BasePage;
import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.FilterFilter;

import java.util.List;


public class DokterJoin {
    private BaseResponse<BasePage<List<Doctor>>> doctors;
    private BaseResponse<FilterFilter> filters;

    public DokterJoin(BaseResponse<BasePage<List<Doctor>>> doctors, BaseResponse<FilterFilter> filters) {
        this.doctors = doctors;
        this.filters = filters;
    }

    public BaseResponse<BasePage<List<Doctor>>> getDoctors() {
        return doctors;
    }

    public void setDoctors(BaseResponse<BasePage<List<Doctor>>> doctors) {
        this.doctors = doctors;
    }

    public BaseResponse<FilterFilter> getFilters() {
        return filters;
    }

    public void setFilters(BaseResponse<FilterFilter> filters) {
        this.filters = filters;
    }
}
