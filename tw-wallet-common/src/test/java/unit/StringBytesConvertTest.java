package unit;

import com.thoughtworks.common.util.dcep.StringBytesConvert;
import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StringBytesConvertTest {

    // 人命币的格式
    String rmb100 = "CB_100_00_0000000000000000000001";
    String rmb50 = "CB_050_00_0000000000000000000001";
    String rmb10 = "CB_010_00_0000000000000000000001";
    String rmb5 = "CB_005_00_0000000000000000000001";
    String rmb1 = "CB_001_00_0000000000000000000001";
    String rmb05 = "CB_000_50_0000000000000000000001";
    String rmb01 = "CB_000_10_0000000000000000000001";

    String uint256_rmb100 = "43425f3130305f30305f30303030303030303030303030303030303030303031";
    String uint256_rmb50 = "43425f3035305f30305f30303030303030303030303030303030303030303031";
    String uint256_rmb10 = "43425f3031305f30305f30303030303030303030303030303030303030303031";
    String uint256_rmb5 = "43425f3030355f30305f30303030303030303030303030303030303030303031";

    @Test
    void should_convert_success() throws Exception {
        {
            Bytes32 bytes32 = StringBytesConvert.StringToBytes32(rmb100);
            BigInteger hexBig = new BigInteger(bytes32.getValue());
            assertThat(hexBig.toString(16)).isEqualTo(uint256_rmb100);
        }
        {
            Bytes32 bytes32 = StringBytesConvert.StringToBytes32(rmb50);
            BigInteger hexBig = new BigInteger(bytes32.getValue());
            assertThat(hexBig.toString(16)).isEqualTo(uint256_rmb50);
        }
        {
            Bytes32 bytes32 = StringBytesConvert.StringToBytes32(rmb10);
            BigInteger hexBig = new BigInteger(bytes32.getValue());
            assertThat(hexBig.toString(16)).isEqualTo(uint256_rmb10);
        }
        {
            Bytes32 bytes32 = StringBytesConvert.StringToBytes32(rmb5);
            BigInteger hexBig = new BigInteger(bytes32.getValue());
            assertThat(hexBig.toString(16)).isEqualTo(uint256_rmb5);
        }
    }
}
