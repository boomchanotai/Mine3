import { useMutation } from "@tanstack/react-query";
import { login, LoginResponse, Payload } from "../../api/auth";
import toast from "react-hot-toast";

export const useBindWalletMutation = () => {
  return useMutation<LoginResponse, Error, Payload>({
    mutationKey: ["login"],
    mutationFn: login,
    onSuccess: () => {
      toast.success("Successfully logged in");
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};
