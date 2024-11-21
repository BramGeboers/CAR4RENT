import AuthService from "@/services/AuthService";
import { StatusMessage } from "@/types/login";
import { useTranslation } from "next-i18next";
import React, { useState } from "react";
import { useRouter } from "next/router";

const Login: React.FC = () => {
  const { t } = useTranslation();
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [role, setRole] = useState<string>("");
  const [errorMessage, setErrorMessage] = useState<StatusMessage>();
  const [statusMessage, setStatusMessage] = useState<StatusMessage>();
  const [emailError, setEmailError] = useState<string>("");
  const [passwordError, setPasswordError] = useState<string>("");
  const [roleError, setRoleError] = useState<string>("");

  const router = useRouter();

  const handleLogin = async (event: React.ChangeEvent<HTMLFormElement>) => {
    event.preventDefault();
    setStatusMessage(undefined);
    if (!validation()) {
      return;
    }

    try {
      const response = await AuthService.register({ email, password, role });
      const data = await response.json();

      if (response.ok) {
        sessionStorage.setItem(
          "user",
          JSON.stringify({
            token: data.token,
            email: data.email,
            role: data.role,
          })
        );

        router.push("/confirm");
      } else if (!response.ok) {
        const errorMessage = data.email.replace('email: ', '');
        console.log(data)
        setErrorMessage({ type: "error", message: t("error.emailD") });
      }
    } catch (error) {
      setStatusMessage({
        type: "error",
        message: t("error.errorTAL"),
      });
    }
  };

  const validation = (): boolean => {
    let isValid = true;
    setEmailError("");
    setPasswordError("");
    setRoleError("");

    if (!email || email.trim() === "") {
      setEmailError(t("error.emailR"));
      isValid = false;
    }
    if (!password || password.trim() === "") {
      setPasswordError(t("error.passwordR"));
      isValid = false;
    }
    if (!role || role.trim() === "") {
      setRoleError(t("error.roleR"));
      isValid = false;
    }

    return isValid;
  };

  return (
    <div className="flex flex-col items-center bg-white rounded-lg max-w-[600px] p-6">
      <h2 className="pb-2 font-semibold text-2xl">{t("general.register")}</h2>
      <form onSubmit={handleLogin} className="flex flex-col items-start">
        {statusMessage && <p>{statusMessage.message}</p>}
        <input
          className="p-2 m-2 max-w-[400px] rounded-md bg-gray-200 text-md"
          type="text"
          name="email"
          onChange={(input) => setEmail(input.target.value)}
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

        <select
          name="building"
          id="lang"
          className="min-w-[197px] bg-gray-200 rounded-md p-2 m-2 mb-1 max-w-[400px] focus:outline-none"
          onChange={(input) => setRole(input.target.value)}
        >
          <option value="">---</option>
          <option value="owner">{t("general.owner")}</option>
          <option value="renter">{t("general.renter")}</option>
        </select>
        {roleError && <span className="text-red-500">{roleError}</span>}
        
        <button
          className="bg-blue-400 px-5 py-2 m-2 rounded-md transition-all hover:bg-white border-2 border-blue-400"
          type="submit"
        >
          {t("general.register")}
        </button>

        {errorMessage && <p className="text-red-500">{errorMessage.message}</p>}
      </form>
    </div>
  );
};

export default Login;
