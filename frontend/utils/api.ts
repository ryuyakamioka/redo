import axios from "axios";
import { useRuntimeConfig } from "#app";

export const useApi = () => {
  const config = useRuntimeConfig();
  return axios.create({
    baseURL: config.public.apiBase,
  });
};
