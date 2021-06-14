package com.transmedika.transmedikakitui.models.bean.join;

import com.transmedika.transmedikakitui.models.bean.json.BaseResponse;
import com.transmedika.transmedikakitui.models.bean.json.Doctor;
import com.transmedika.transmedikakitui.models.bean.json.GLAccount;
import com.transmedika.transmedikakitui.models.bean.json.Profile;

import java.util.List;


public class KeluargaJoin {
    private BaseResponse<Profile> profil;
    private BaseResponse<List<Profile>> keluargas;
    private BaseResponse<Doctor> dokter;
    private BaseResponse<GLAccount> glAccount;

    public KeluargaJoin() {
    }

    public KeluargaJoin(BaseResponse<Profile> profil, BaseResponse<List<Profile>> keluargas,
                        BaseResponse<Doctor> dokter, BaseResponse<GLAccount> glAccount) {
        this.profil = profil;
        this.keluargas = keluargas;
        this.dokter = dokter;
        this.glAccount = glAccount;
    }

    public BaseResponse<Profile> getProfil() {
        return profil;
    }

    public void setProfil(BaseResponse<Profile> profil) {
        this.profil = profil;
    }

    public BaseResponse<List<Profile>> getKeluargas() {
        return keluargas;
    }

    public void setKeluargas(BaseResponse<List<Profile>> keluargas) {
        this.keluargas = keluargas;
    }

    public BaseResponse<Doctor> getDokter() {
        return dokter;
    }

    public void setDokter(BaseResponse<Doctor> dokter) {
        this.dokter = dokter;
    }

    public BaseResponse<GLAccount> getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(BaseResponse<GLAccount> glAccount) {
        this.glAccount = glAccount;
    }
}
