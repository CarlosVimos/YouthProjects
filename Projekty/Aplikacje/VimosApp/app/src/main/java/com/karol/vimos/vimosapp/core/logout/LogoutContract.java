package com.karol.vimos.vimosapp.core.logout;

/**
 * Created by Vimos on 15/02/17.
 */

public interface LogoutContract {
    interface View {
        void onLogoutSuccess(String message);

        void onLogoutFailure(String message);
    }

    interface Presenter {
        void logout();
    }

    interface Interactor {
        void performFirebaseLogout();
    }

    interface OnLogoutListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
