const getItem = (key: string) => {
  if (typeof window === "undefined") return null;
  return sessionStorage.getItem(key);
};

const setItem = (key: string, value: string) => {
  if (typeof window === "undefined") return null;
  sessionStorage.setItem(key, value);
};

const removeItem = (key: string) => {
  if (typeof window === "undefined") return null;
  sessionStorage.removeItem(key);
};

const clear = () => {
  if (typeof window === "undefined") return null;
  sessionStorage.clear();
};

const isOwner = () => {
  if (typeof window === "undefined") return null;
  return sessionStorage.getItem("role") === "OWNER";
};

const isRenter = () => {
  if (typeof window === "undefined") return null;
  return sessionStorage.getItem("role") === "RENTER";
};

const isAccountant = () => {
  if (typeof window === "undefined") return null;
  return sessionStorage.getItem("role") === "ACCOUNTANT";
};

const isAdmin = () => {
  if (typeof window === "undefined") return null;
  return sessionStorage.getItem("role") === "ADMIN";
};

const getRole = () => {
  if (typeof window === "undefined") return null;
  return sessionStorage.getItem("role");
}

const isLoggedIn = () => {
  if (typeof window === "undefined") return null;
  return (
    sessionStorageService.getItem("token") !== null &&
    sessionStorageService.getItem("token") !== undefined
  );
};

export const sessionStorageService = {
  getItem,
  setItem,
  removeItem,
  clear,
  isOwner,
  isRenter,
  isAccountant,
  isAdmin,
  isLoggedIn,
  getRole
};
