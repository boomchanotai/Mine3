import { Container } from "../components/Container";

const Index = () => {
  return (
    <main className="min-h-[80vh] mt-8">
      <Container className="space-y-4">
        <h1 className="font-bold text-2xl">Mine3</h1>
        <div>
          Log in to your Minecraft server using Web3 wallets like OKX or
          MetaMask. Set up your wallet, enter the server address, choose your
          wallet, and authorize the connection. Enjoy features like in-game
          asset ownership and trading, merging Minecraft with blockchain
          technology.
        </div>
        <div className="flex gap-2">
          <a href="https://github.com/boomchanotai/Mine3" target="_blank">
            <button
              type="button"
              className="outline-none bg-black py-2 px-6 rounded-2xl shadow-lg text-white"
            >
              Github
            </button>
          </a>
          <a href="https://boomchanotai.com/" target="_blank">
            <button
              type="button"
              className="outline-none bg-black py-2 px-6 rounded-2xl shadow-lg text-white"
            >
              Author Website
            </button>
          </a>
        </div>
      </Container>
    </main>
  );
};

export default Index;
