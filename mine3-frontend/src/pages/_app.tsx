import { Outlet } from "react-router-dom";
import { ConnectButton } from "@rainbow-me/rainbowkit";
import logo from "../assets/logo.png";
import { Container } from "../components/Container";
import { Toaster } from "react-hot-toast";

const Layout = () => {
  return (
    <div>
      <Toaster />

      <Container className="flex flex-row justify-between items-center py-4">
        <div className="flex flex-row items-center gap-6">
          <div>
            <img
              src={logo}
              alt="Mine3"
              className="object-cover object-center size-10"
            />
          </div>
        </div>
        <div>
          <ConnectButton />
        </div>
      </Container>

      <Outlet />
    </div>
  );
};

export default Layout;
