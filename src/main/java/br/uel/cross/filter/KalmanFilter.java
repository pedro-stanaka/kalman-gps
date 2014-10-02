package br.uel.cross.filter;

import br.uel.cross.filter.utils.Matrix;

/**
 *
 */
public class KalmanFilter {

    /* k */
    private static int timeStep;

    /* These parameters define the size of the matrices. */
    private int stateDimension, observationDimension;

    /* This group of matrices must be specified by the user. */
  /* F_k */
    private Matrix stateTransition;
    /* H_k */
    private Matrix observationModel;
    /* Q_k */
    private Matrix processNoiseCovariance;
    /* R_k */
    private Matrix observationNoiseCovariance;

    /* The observation is modified by the user before every time step. */
  /* z_k */
    private Matrix observation;

    /* This group of matrices are updated every time step by the filter. */
  /* x-hat_k|k-1 */
    private Matrix predictedState;
    /* P_k|k-1 */
    private Matrix predictedEstimateCovariance;
    /* y-tilde_k */
    private Matrix innovation;
    /* S_k */
    private Matrix innovationCovariance;
    /* S_k^-1 */
    private Matrix inverseInnovationCovariance;
    /* K_k */
    private Matrix optimalGain;
    /* x-hat_k|k */
    private Matrix stateEstimate;
    /* P_k|k */
    private Matrix estimateCovariance;

    /* This group is used for meaningless intermediate calculations */
    private Matrix verticalScratch;
    private Matrix smallSquareScratch;
    private Matrix bigSquareScratch;

    public KalmanFilter(int stateDimension, int observationDimension) {
        timeStep = 0;
        this.stateDimension = stateDimension;
        this.observationDimension = observationDimension;

        this.stateTransition = new Matrix(stateDimension, stateDimension);
        this.observationModel = new Matrix(observationDimension, stateDimension);
        this.processNoiseCovariance = new Matrix(stateDimension, stateDimension);
        this.observationNoiseCovariance = new Matrix(observationDimension, observationDimension);

        this.observation = new Matrix(observationDimension, 1);

        this.predictedState = new Matrix(stateDimension, 1);
        this.predictedEstimateCovariance = new Matrix(stateDimension, stateDimension);

        this.innovation = new Matrix(observationDimension, 1);
        this.innovationCovariance = new Matrix(observationDimension, observationDimension);
        this.inverseInnovationCovariance = new Matrix(observationDimension, observationDimension);

        this.optimalGain = new Matrix(stateDimension, observationDimension);
        this.stateEstimate = new Matrix(stateDimension, 1);
        this.estimateCovariance = new Matrix(stateDimension, stateDimension);

        this.verticalScratch = new Matrix(stateDimension, observationDimension);
        this.smallSquareScratch = new Matrix(observationDimension, observationDimension);
        this.bigSquareScratch = new Matrix(stateDimension, stateDimension);
    }


    /**
     *  Runs one timestep of prediction + estimation.
     *  Before each time step of running this, set f.observation to be the
     *  next time step's observation.
     *  Before the first step, define the model by setting:
     *      stateTransition
     *      observationModel
     *      processNoiseCovariance
     *      observationNoiseCovariance
     *
     *
     *  It is also advisable to initialize with reasonable guesses for
     *      state_estimate
     *      estimate_covariance
     */
    public void update() {
        this.predict();
        this.estimate();
    }

    private void predict() {
        timeStep++;

        // Predict state
        predictedState = stateTransition.multipliedBy(stateEstimate);

        // Predict the state estimate covariance
        bigSquareScratch = stateTransition.multipliedBy(estimateCovariance);
        predictedEstimateCovariance = bigSquareScratch.multiplyByTranspose(stateTransition);
        predictedEstimateCovariance = predictedEstimateCovariance.addMatrix(processNoiseCovariance);
    }

    private void estimate() {
        //TODO: Modified some operations to inline forms, if something break, try fix here
        innovation = observationModel.multipliedBy(predictedState);
        innovation = observation.subtractMatrix(innovation);

        verticalScratch = predictedEstimateCovariance.multiplyByTranspose(observationModel);
        innovationCovariance = observationModel.multipliedBy(verticalScratch);
        innovationCovariance = innovationCovariance.addMatrix(observationNoiseCovariance);


        inverseInnovationCovariance = innovationCovariance.inverse();

        /* Calculate the optimal Kalman gain.
             Note we still have a useful partial product in vertical scratch
             from the innovation covariance. */
        optimalGain = verticalScratch.multipliedBy(inverseInnovationCovariance);

        stateEstimate = optimalGain.multipliedBy(innovation).addMatrix(predictedState);


        bigSquareScratch = optimalGain.multipliedBy(observationModel);
        bigSquareScratch.subtractFromIdentity();

        estimateCovariance = bigSquareScratch.multipliedBy(predictedEstimateCovariance);
    }


    public Matrix getStateTransition() {
        return stateTransition;
    }

    public Matrix getProcessNoiseCovariance() {
        return processNoiseCovariance;
    }

    public Matrix getObservationNoiseCovariance() {
        return observationNoiseCovariance;
    }

    public Matrix getStateEstimate() {
        return stateEstimate;
    }

    public Matrix getEstimateCovariance() {
        return estimateCovariance;
    }

    public void setStateTransition(double... doubles){
        this.stateTransition.setMatrix(doubles);
    }

    public void setObservationModel(double... doubles){
        this.observationModel.setMatrix(doubles);
    }

    public void setObservation(double... doubles) {
        this.observation.setMatrix(doubles);
    }

    public void setStateEstimate(double... doubles) {
        this.stateEstimate.setMatrix(doubles);
    }

    public void setProcessNoiseCovariance(double... doubles) {
        this.processNoiseCovariance.setMatrix(doubles);
    }

}
