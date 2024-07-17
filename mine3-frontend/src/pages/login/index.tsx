import { useAccount, useSignMessage } from "wagmi";
import { useCallback, useMemo } from "react";
import { Container } from "../../components/Container";
import { Navigate, useSearchParams } from "react-router-dom";
import { useBindWalletMutation } from "../../hook/mutation/useBindWalletMutation";

const Login = () => {
  const [searchParams] = useSearchParams();

  const token = useMemo(() => {
    return searchParams.get("token");
  }, [searchParams]);

  const { isConnected, address } = useAccount();
  const { signMessageAsync } = useSignMessage();
  const { mutate } = useBindWalletMutation();

  const handleSignMessage = useCallback(async () => {
    if (!token || !address) return;

    const timestamp = new Date().getTime();

    try {
      const signature = await signMessageAsync({
        message: `Sign in to Mine3 and this is my wallet address: ${address} to sign in ${token}. now is ${timestamp}`,
      });

      const payload = {
        address,
        token,
        signature,
        timestamp,
      };

      mutate(payload);
    } catch (error) {
      throw new Error("Failed to sign message");
    }
  }, [token, address, signMessageAsync, mutate]);

  if (!token) {
    return <Navigate to="/" />;
  }

  return (
    <main className="flex flex-col justify-center items-center min-h-[50vh]">
      <Container>
        {isConnected && (
          <div>
            <button
              onClick={handleSignMessage}
              className="outline-none bg-blue-500 py-2 px-6 rounded-2xl shadow-lg font-bold text-white"
            >
              Bind EVM Wallet
            </button>
          </div>
        )}
      </Container>
    </main>
  );
};

export default Login;
