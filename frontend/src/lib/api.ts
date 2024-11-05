const toKR = (value: number | undefined) => {
    return value?.toLocaleString('ko-KR', { maximumFractionDigits: 8 }) ?? 0;
}

const checkNaN = (value: number) => {
    return isNaN(value) ? 0 : value
}

export { toKR, checkNaN }