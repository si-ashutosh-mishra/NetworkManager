import com.sportzinteractive.network.helper.ApiResult
import com.sportzinteractive.network.helper.NetworkThrowable
import com.sportzinteractive.network.helper.Resource


abstract class ApiResultHandler<Result, Data>(
    private val response: ApiResult<Result?>,
) {

    suspend fun getResult(): Resource<Data?> {

        return when (response) {

            is ApiResult.GenericError -> {
                Resource.Error(throwable = NetworkThrowable(response.code, "Something went wrong."))
            }

            is ApiResult.NetworkError -> {
                Resource.Error(throwable = NetworkThrowable(null, response.message ?: ""))
            }

            is ApiResult.Success -> {
                if (response.data == null) {
                    Resource.Error(throwable = NetworkThrowable(null, "No response from api."))
                } else {
                    handleSuccess(resultObj = response.data)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Result): Resource<Data?>

}



