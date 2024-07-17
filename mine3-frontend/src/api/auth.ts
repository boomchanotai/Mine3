interface LoginResponse {
  result: string;
}

interface Payload {
  address: string;
  token: string;
  signature: string;
  timestamp: number;
}

const login = async ({ address, token, signature, timestamp }: Payload) => {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      address,
      token,
      signature,
      timestamp,
    }),
  });

  if (!response.ok) {
    throw new Error("Failed to login");
  }

  return response.json();
};

export { login };
export type { LoginResponse, Payload };
