package com.karol.vimos.vimosapp.core.red;

/**
 * Created by Vimos on 21/02/17.
 */

public interface SetRedPositionContract {
    interface View {

        void onSetRedPositionSuccess(String message);

        void onSetRedPositionFailure(String message);

    }

    interface Presenter {

        void setRedPosition(String id);

    }

    interface Interactor {

        void setRedPositionToDatabase(String id);
    }

    interface OnSetRedPositionListener {

        void onSetRedPositionSuccess(String message);

        void onSetRedPositionFailure(String message);

    }
}
