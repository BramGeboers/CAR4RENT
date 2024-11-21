// LoginService.ts
import { Login, Register } from "@/types/login";

let baseUrl = process.env.NEXT_PUBLIC_API_URL || "https://team15-backend.azurewebsites.net";
console.log('API URL:', baseUrl);

const login = ({ email, password }: Login) => {
  if (!baseUrl) {
    baseUrl = "https://team15-backend.azurewebsites.net";
  }
  const data = {
    email: email,
    password: password,
  };

  return fetch(baseUrl + "/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "Application/json",
    },
    body: JSON.stringify(data),
  });
};

const register = ({ email, password, role }: Register) => {
  if (!baseUrl) {
    baseUrl = "https://team15-backend.azurewebsites.net";
  }
    const data = {
      email: email,
      password: password,
      role: role,
    };
  
    return fetch(baseUrl + "/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "Application/json",
      },
      body: JSON.stringify(data),
    });
  };

  const logout = () => {  
    if (!baseUrl) {
      baseUrl = "https://team15-backend.azurewebsites.net";
    }
    return fetch(baseUrl + "/auth/logout", {
      method: "POST",
      headers: {
        "Content-Type": "Application/json",
      },
    });
  };

const LoginService = {
  login,
  register,
  logout
};

export default LoginService;