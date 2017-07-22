package com.karol.vimos.vimosapp.core.red;

/**
 * Created by Vimos on 21/02/17.
 */

public class SetRedPositionPresenter implements SetRedPositionContract.Interactor, SetRedPositionContract.OnSetRedPositionListener {

    private SetRedPositionContract.View mView;
    private SetRedPositionInteractor mSetRedPositionInteractor;

    public SetRedPositionPresenter(SetRedPositionContract.View view) {
        this.mView = view;
        mSetRedPositionInteractor = new SetRedPositionInteractor(this);
    }

    @Override
    public void setRedPositionToDatabase(String id) {
        mSetRedPositionInteractor.setRedPositionToDatabase(id);

    }

    @Override
    public void onSetRedPositionSuccess(String message) {

    }

    @Override
    public void onSetRedPositionFailure(String message) {

    }
}
