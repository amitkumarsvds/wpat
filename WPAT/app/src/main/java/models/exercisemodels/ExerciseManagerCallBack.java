package models.exercisemodels;


import java.util.List;

/**
 * callback to handle the response for different scenario
 */
public interface ExerciseManagerCallBack {

    void UDManagerFailureCallback(String failureData);
    void UDManagerSuccessCallback(List<Row> e);
}
