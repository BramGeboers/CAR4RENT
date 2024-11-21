import AuthService from "@/services/AuthService";
import { StatusMessage } from "@/types/login";
import { useTranslation } from "next-i18next";
import React, { useState } from "react";
import { json } from "stream/consumers";
import { useRouter } from "next/router";

const Login: React.FC = () => {
  const { t } = useTranslation();
  const [email, setemail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const [statusMessage, setStatusMessage] = useState<StatusMessage>();
  const [emailError, setemailError] = useState<string>("");
  const [passwordError, setPasswordError] = useState<string>("");

  const router = useRouter();

  const handleLogin = async (event: React.ChangeEvent<HTMLFormElement>) => {
    event.preventDefault();
    setStatusMessage(undefined);
    if (!validation()) {
      return;
    }
    
    console.log("validation passed")
    try {
      const response = await AuthService.login({ email, password });
      const data = await response.json();

      if (response.ok) {
        console.log(data.token);
        console.log(data.email);
        console.log(data.role);

        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem("email", data.email);
        sessionStorage.setItem("role", data.role);

        window.location.href = "/";
      } else if (!response.ok) {
        if (response.status === 401) {
          setStatusMessage({ type: "error", message: t("error.invalidCredentials") });
          console.log("error: " + t("error.invalidCredentials"));
        }

        else if (response.status === 403) {
          setStatusMessage({ type: "error", message: t("error.forbidden") });
          console.log("error: " + t("error.forbidden"));
        }

        else if (response.status === 500) {
          setStatusMessage({ type: "error", message: t("error.serverError") });
          console.log("error: " + t("error.serverError"));
        }

        else {
          setStatusMessage({ type: "error", message: data.message });
          console.log("error: " + data.message);
        }
      }
    } catch (error) {
      setStatusMessage({
        type: "error",
        message: t("error.errorTAL"),
      });
    }
  };

  const validation = (): boolean => {
    let count = 0;
    setemailError("");
    setPasswordError("");
    if (!email || email.trim() === "") {
      setemailError(t("error.emailR"));
      count += 1;
    }
    if (!password || password.trim() === "") {
      setPasswordError(t("error.passwordR"));
      count += 1;
    }
    return count === 0;
  };

  return (
    <div className="flex flex-col items-center bg-white rounded-lg max-w-[600px] p-6">
      <h2 className="pb-2 font-semibold text-2xl">Login</h2>
      <form onSubmit={handleLogin} className="flex flex-col items-start">
        {statusMessage && <p>{statusMessage.message}</p>}
        <input
          className="p-2 m-2 max-w-[400px] rounded-md bg-gray-200 text-md"
          type="text"
          name="email"
          onChange={(input) => setemail(input.target.value)}
          placeholder={t("rent.email")}
        />
        {emailError && <span>{emailError}</span>}
        <input
          className="p-2 m-2 max-w-[400px] rounded-md bg-gray-200 text-md"
          type="password"
          name="password"
          onChange={(input) => setPassword(input.target.value)}
          placeholder={t("general.password")}
        />
        {passwordError && <span>{passwordError}</span>}
        <button
          className="bg-blue-400 px-5 py-2 m-2 rounded-md transition-all hover:bg-white border-2 border-blue-400"
          type="submit"
        >
          {t("general.login")}
        </button>
      </form>
    </div>
  );
};

export default Login;
